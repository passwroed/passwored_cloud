<template>
    <el-container>
        <el-header height="70px" class="header-container">
            <div class="logo">
                <div class="logo-text">LOGO</div>
                <!--                <img src="../assets/image/logo.png"/>-->
            </div>
            <div v-for="(first) in firstMenu" :class="first.className" @click="firstMenuClick(first)">
                <i :class="first.icon"/>
                <div>{{ first.name }}</div>
            </div>
            <el-dropdown>
                <el-avatar icon="el-icon-user-solid"></el-avatar>
                <el-dropdown-menu slot="dropdown" class="main-dropdown-menu">
                    <el-dropdown-item icon="el-icon-key" @click.native="showUpdatePassDialog">密码修改</el-dropdown-item>
                    <el-dropdown-item icon="el-icon-circle-close" @click.native="logoutSys">退出系统</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </el-header>
        <el-container>
            <el-aside width="220px" class="left-container">
                <div class="aside-title">{{ firstMenuTitle }}</div>
                <el-menu>
                    <el-submenu v-for="(second) in secondMenu" v-if="parseInt(second.type) === 0" :index="second.id">
                        <span slot="title">{{ second.name }}</span>
                        <el-menu-item v-for="(three) in second.childList" :index="three.id" @click="addTab(three)">
                            {{ three.name }}
                        </el-menu-item>
                    </el-submenu>
                    <el-menu-item v-for="(second) in secondMenu" v-if="parseInt(second.type) === 1" :index="second.id"
                                  @click="addTab(second)">{{ second.name }}
                    </el-menu-item>
                </el-menu>
            </el-aside>
            <el-main class="main-container">
                <el-tabs v-model="tabSelected" type="card" @tab-remove="removeTab">
                    <el-tab-pane
                        v-for="(tab) in tabData"
                        :label="tab.name"
                        :name="tab.id"
                        :closable="tab.closable">
                        <component :is=tab.content></component>
                    </el-tab-pane>
                </el-tabs>
            </el-main>
        </el-container>
        <el-dialog title="密码修改" :visible.sync="updatePassDialogVisible" width="400px">
            <el-form :model="updatePassForm" label-width="80px">
                <el-form-item label="旧密码">
                    <el-input v-model="updatePassForm.oldPass" autocomplete="off" show-password></el-input>
                </el-form-item>
                <el-form-item label="新密码">
                    <el-input v-model="updatePassForm.newPass" autocomplete="off" show-password></el-input>
                </el-form-item>
                <el-form-item label="重复密码">
                    <el-input v-model="updatePassForm.newPassRepeat" autocomplete="off" show-password></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="updatePassDialogVisible=false">取消</el-button>
                <el-button type="primary" @click="updatePass">确定</el-button>
            </div>
        </el-dialog>
    </el-container>
</template>

<script>

export default {
    data() {
        return {
            firstMenu: [], // 1级菜单
            firstMenuTitle: null, // 当前1级菜单名称
            secondMenu: [], // 当前2级菜单
            tabSelected: "index", // 选项卡选中项目
            tabData: [{
                id: "index",
                name: "系统主页",
                closable: false,
                content: () => import("./index.vue")
            }],
            updatePassDialogVisible: false, //密码修改对话框
            updatePassForm: {} // 密码修改表单内容
        }
    },
    created() {
        this.refreshTree();
    },
    methods: {
        refreshTree() {
            this.$http.request("/apiUser/admin/getAdminPermissionList", {}, response => {
                let authMenu = this.$util.getChildList(response.list, "0", "id");
                if (authMenu.length === 0) {
                    return;
                }
                // 给最外层的增加样式
                for (let i = 0; i < authMenu.length; i++) {
                    let className = "first-menu";
                    if (i === 0) {
                        className += " selected";
                    }
                    authMenu[i].className = className;
                }

                this.firstMenu = authMenu;
                this.firstMenuTitle = authMenu[0].name;
                this.secondMenu = authMenu[0].childList;
            });
        },
        firstMenuClick(menu) {
            for (let i = 0; i < this.firstMenu.length; i++) {
                let curMenu = this.firstMenu[i];
                let className = "first-menu";
                if (curMenu.id === menu.id) {
                    className += " selected";
                    this.firstMenuTitle = curMenu.name;
                    this.secondMenu = curMenu.childList;
                }
                curMenu.className = className;
            }
        },
        addTab(menu) {
            let isFind = false;
            if (parseInt(menu.type) !== 1) {
                return;
            }
            // 检查是否已存在
            this.tabData.forEach((tab) => {
                if (tab.id === menu.id) {
                    isFind = true;
                }
            });
            this.tabSelected = menu.id;
            if (isFind) {
                return;
            }
            this.tabData.push({
                id: menu.id,
                name: menu.name,
                closable: true,
                content: () => import("../views" + menu.url)
            });
        },
        removeTab(targetId) {
            let data = this.tabData;
            let selectedId = this.tabSelected;
            if (selectedId === targetId) {
                data.forEach((tab, index) => {
                    if (tab.id === targetId) {
                        let nextTab = data[index + 1] || data[index - 1];
                        if (nextTab) {
                            selectedId = nextTab.id;
                        }
                    }
                });
            }

            this.tabSelected = selectedId;
            this.tabData = data.filter(tab => tab.id !== targetId);
        },
        logoutSys() {
            this.$confirm("您确定要退出系统吗？", "提示").then(() => {
                this.$http.request("/api/manage/sys/login/userLogout", {}, response => {
                    if (!response.success) {
                        this.$message({
                            showClose: true,
                            message: response.message,
                            type: 'warning'
                        });
                        return;
                    }
                    sessionStorage.removeItem("loginToken");
                    this.$router.push("/login");
                });
            });
        },
        showUpdatePassDialog() {
            this.updatePassDialogVisible = true;
            this.updatePassForm = {};
        },
        updatePass() {
            if (this.updatePassForm["newPass"] !== this.updatePassForm["newPassRepeat"]) {
                this.$message({
                    showClose: true,
                    message: "两次输入的密码不一致，请检查！",
                    type: 'warning'
                });
                return;
            }
            let params = {...this.updatePassForm};
            this.$confirm("您确定要修改密码吗？", "提示").then(() => {
                this.$http.request("/api/manage/sys/login/updatePassword", params, response => {
                    if (!response.success) {
                        this.$message({
                            showClose: true,
                            message: response.message,
                            type: 'warning'
                        });
                        return;
                    }
                    this.updatePassDialogVisible = false;
                });
            });
        }
    }
}
</script>

<style lang="less">
@import "../assets/less/default.less";

.header-container {
    padding: 0;
    border-bottom: 1px solid @border-color;

    .logo {
        float: left;
        width: 220px;
        height: 70px;

        .logo-text {
            font-weight: bold;
            line-height: 70px;
            font-size: 40px;
            color: #5daf34;
            text-align: center;
        }

        img {
            margin-left: 40px;
            margin-top: 10px;
            width: 120px;
        }
    }

    .first-menu {
        float: left;
        width: 80px;
        height: 70px;
        text-align: center;
        color: #29A348;

        i {
            font-size: 24px;
            margin-top: 15px;
        }

        div {
            font-size: 12px;
        }

        &.selected {
            background-color: @border-color;
        }
    }

    .el-dropdown {
        float: right;
        margin-top: 15px;
        margin-right: 30px;
    }

    .el-avatar {
        background-color: @main-color;
    }
}

.main-dropdown-menu {
    .el-dropdown-menu__item {
        font-size: 12px;
        height: 40px;
        line-height: 40px;
        width: 100px;
        text-align: center;
        padding-right: 15px;
    }
}

.left-container {
    background-color: @background-color;

    .aside-title {
        font-size: 14px;
        font-weight: bold;
        background-color: @border-color;
        height: 40px;
        line-height: 40px;
        padding-left: 20px;
    }

    .el-menu {
        border: 0;
        background-color: @background-color;

        .el-submenu {
            border-bottom: 1px solid @border-color;

            div, span {
                font-size: 12px;
                height: 40px;
                line-height: 40px;
                background-color: @background-color;
            }
        }

        .el-menu-item {
            font-size: 12px;
            height: 40px;
            line-height: 40px;
            background-color: @background-color;

            &.is-active {
                background-color: @border-color;
                color: #303133;
            }
        }
    }
}

.main-container {
    padding: 0;
    background-color: white;
}
</style>