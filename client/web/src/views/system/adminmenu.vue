<template>
    <div class="tree-container">
        <div class="left-tree">
            <el-tree :data="treeData" :props="treeProps" @node-click="treeClick"></el-tree>
        </div>
        <div class="right-detail">
            <el-tabs type="card" v-model="tabSelectedName" @tab-click="tabClick">
                <el-tab-pane name="editMenu" label="修改菜单">
                    <el-form :model="editMenu" :inline="true" label-width="80px" style="margin-top: 20px">
                        <el-row>
                            <el-col style="width: 340px">
                                <el-form-item label="菜单名称">
                                    <el-input v-model="editMenu.name"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col style="width: 340px">
                                <el-form-item label="链接">
                                    <el-input v-model="editMenu.url"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col style="width: 340px">
                                <el-form-item label="图标">
                                    <el-input v-model="editMenu.icon"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col style="width: 340px">
                                <el-form-item label="备注">
                                    <el-input v-model="editMenu.description"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col style="width: 340px">
                                <el-form-item label="权限类型">
                                    <el-radio-group v-model="editMenu.type">
                                        <el-radio :label="0">栏目</el-radio>
                                        <el-radio :label="1">页面</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col style="width: 600px;text-align:center;">
                                <el-button class="el-icon-coin" :disabled="!isMenuSelected" @click="updateEditMenu">保存</el-button>
                                <el-button class="el-icon-delete" :disabled="!isMenuSelected" @click="deleteEditMenu">删除</el-button>
                                <el-button class="el-icon-refresh" @click="refreshTree">刷新菜单</el-button>
                                <el-button class="el-icon-circle-plus-outline" v-if="parseInt(editMenu.type) === 1" :disabled="!isMenuSelected"
                                           @click="showNewMenuApiDialog">添加页面接口
                                </el-button>
                            </el-col>
                        </el-row>
                    </el-form>
                    <el-table v-if="parseInt(editMenu.type) === 1" :data="menuApi" border style="width:90%;margin-left:20px;margin-top:10px;">
                        <el-table-column type="index" :index="indexMethod" label="序号" width="80"></el-table-column>
                        <el-table-column prop="name" label="接口名称"></el-table-column>
                        <el-table-column prop="url" label="接口地址"></el-table-column>
                        <el-table-column label="操作" width="180">
                            <template slot-scope="scope">
                                <el-button class="el-icon-edit" @click="showEditMenuApiDialog(scope.row)">修改</el-button>
                                <el-button class="el-icon-delete" @click="deleteMenuApi(scope.row)">删除</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-tab-pane>
                <el-tab-pane name="insertSameLevelMenu" label="添加同级菜单">
                    <el-form :model="insertSameLevelMenu" :inline="true" label-width="80px" style="margin-top: 20px">
                        <el-row>
                            <el-col style="width: 340px">
                                <el-form-item label="菜单名称">
                                    <el-input v-model="insertSameLevelMenu.name"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col style="width: 340px">
                                <el-form-item label="链接">
                                    <el-input v-model="insertSameLevelMenu.url"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col style="width: 340px">
                                <el-form-item label="图标">
                                    <el-input v-model="insertSameLevelMenu.icon"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col style="width: 340px">
                                <el-form-item label="备注">
                                    <el-input v-model="insertSameLevelMenu.description"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col style="width: 340px">
                                <el-form-item label="权限类型">
                                    <el-radio-group v-model="insertSameLevelMenu.type">
                                        <el-radio :label="0">栏目</el-radio>
                                        <el-radio :label="1">页面</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col style="width: 600px;text-align:center;">
                                <el-button class="el-icon-coin" @click="saveSameLevelMenuApi">保存</el-button>
                                <el-button class="el-icon-refresh" @click="refreshTree">刷新菜单</el-button>
                            </el-col>
                        </el-row>
                    </el-form>
                </el-tab-pane>
                <el-tab-pane name="insertNextLevelMenu" label="添加下级菜单">
                    <el-form :model="insertNextLevelMenu" :inline="true" label-width="80px" style="margin-top: 20px">
                        <el-row>
                            <el-col style="width: 340px">
                                <el-form-item label="菜单名称">
                                    <el-input v-model="insertNextLevelMenu.name"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col style="width: 340px">
                                <el-form-item label="链接">
                                    <el-input v-model="insertNextLevelMenu.url"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col style="width: 340px">
                                <el-form-item label="图标">
                                    <el-input v-model="insertNextLevelMenu.icon"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col style="width: 340px">
                                <el-form-item label="备注">
                                    <el-input v-model="insertNextLevelMenu.description"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col style="width: 340px">
                                <el-form-item label="权限类型">
                                    <el-radio-group v-model="insertNextLevelMenu.type">
                                        <el-radio :label="0">栏目</el-radio>
                                        <el-radio :label="1">页面</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col style="width: 600px;text-align:center;">
                                <el-button class="el-icon-coin" @click="saveNextLevelMenuApi">保存</el-button>
                                <el-button class="el-icon-refresh" @click="refreshTree">刷新菜单</el-button>
                            </el-col>
                        </el-row>
                    </el-form>
                </el-tab-pane>
            </el-tabs>
        </div>
        <el-dialog title="页面接口" :visible.sync="menuApiDialogVisible" width="400px">
            <el-form :model="menuApiForm" label-width="80px">
                <el-form-item label="接口名称">
                    <el-input v-model="menuApiForm.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="接口地址">
                    <el-input v-model="menuApiForm.url" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="menuApiDialogVisible=false">取消</el-button>
                <el-button type="primary" @click="saveMenuApi">确定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>

export default {
    name: "adminmenu",
    data() {
        return {
            treeData: [],
            treeProps: {
                children: "childList",
                label: "name",
            },
            isMenuSelected: false,
            tabSelectedName: "editMenu",
            editMenu: {
                type: 0,
            },
            menuApi: [],
            insertSameLevelMenu: {
                type: 0,
            },
            insertNextLevelMenu: {
                type: 0,
            },
            menuApiDialogVisible: false,
            menuApiForm: {}
        }
    },
    created() {
        // 刷新树形菜单
        this.refreshTree();
    },
    methods: {
        refreshTree() {
            this.$http.request("/apiUser/admin/permission/allList", {}, response => {
                let list = response.list.filter(item => parseInt(item.type) !== 2);
                this.treeData = this.$util.getChildList(list, "0", "id");
            });
        },
        refreshMenuApi(id) {
            id = this.$util.isBlank(id) ? this.editMenu["id"] : id;
            this.$http.request("/apiUser/admin/permission/getButtonListById", {id: id}, response => {
                this.menuApi = response.list;
            });
        },
        indexMethod(index) {
            return index + 1;
        },
        treeClick(data) {
            this.editMenu = data;
            if (parseInt(data.type) === 1) {
                this.refreshMenuApi(data.id);
            }
            this.isMenuSelected = true;
            this.tabSelectedName = "editMenu";
        },
        updateEditMenu() {
            let params = {
                id: this.editMenu.id,
                pid: this.editMenu.pid,
                name: this.editMenu.name,
                type: this.editMenu.type,
                icon: this.editMenu.icon,
                url: this.editMenu.url,
                description: this.editMenu.description
            }
            this.$http.request("/apiUser/admin/permission/editPermission", params, response => {
                this.$alert("保存成功，请点击刷新菜单查看结果。");
            });
        },
        deleteEditMenu() {
            let id = this.editMenu["id"];
            let name = this.editMenu["name"];
            this.$confirm("您确定要删除[" + name + "]菜单吗?", "提示").then(() => {
                this.$http.request("/apiUser/admin/permission/delPermission", {
                    id: id
                }, response => {
                    this.isMenuSelected = false;
                    this.editMenu = {
                        type: 0,
                    };
                    this.refreshTree();
                    this.menuApi = [];
                });
            });
        },
        showNewMenuApiDialog() {
            this.menuApiForm = {};
            this.menuApiDialogVisible = true;
        },
        showEditMenuApiDialog(row) {
            this.menuApiForm = {...row};
            this.menuApiDialogVisible = true;
        },
        saveMenuApi() {
            let apiUrl = "/apiUser/admin/permission/editPermission";
            if (this.menuApiForm.id == null) {
                apiUrl = "/apiUser/admin/permission/addPermission";
            }
            this.menuApiForm.pid = this.editMenu["id"];
            this.menuApiForm.type = 2;
            this.$confirm("您确定要保存页面按钮吗?", "提示").then(() => {
                this.$http.request(apiUrl, this.menuApiForm, response => {
                    this.refreshMenuApi();
                    this.menuApiDialogVisible = false;
                });
            });
        },
        deleteMenuApi(row) {
            let name = row["name"];
            this.$confirm("您确定要删除该菜单接口[" + name + "]吗?", "提示").then(() => {
                this.$http.request("/apiUser/admin/permission/delPermission", {
                    id: row["id"],
                }, response => {
                    this.refreshMenuApi();
                    this.menuApiDialogVisible = false;
                });
            });
        },
        tabClick(tab) {
            let id = this.editMenu["id"];
            let pid = this.editMenu["pid"];
            if (id == null) {
                id = 0;
            }
            if (pid == null) {
                pid = 0;
            }
            switch (parseInt(tab.index)) {
                case 1:
                    this.insertSameLevelMenu = {
                        pid: pid,
                        type: this.editMenu["type"],
                    };
                    break;
                case 2:
                    this.insertNextLevelMenu = {
                        pid: id,
                        type: 1,
                    };
                    break;
            }
        },
        saveSameLevelMenuApi() {
            this.$http.request("/apiUser/admin/permission/addPermission", this.insertSameLevelMenu, response => {
                this.$alert("保存成功，请点击刷新菜单查看结果。");
            });

        },
        saveNextLevelMenuApi() {
            this.$http.request("/apiUser/admin/permission/addPermission", this.insertNextLevelMenu, response => {
                this.$alert("保存成功，请点击刷新菜单查看结果。");
            });
        }
    }
}
</script>

<style lang="less">
</style>