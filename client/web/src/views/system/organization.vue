<template>
    <el-row>
        <el-col :span="12">
            <div style="margin-left:10px;margin-top:10px;">
                <span class="font-size-12">名称:</span>
                <el-input v-model="roleName" placeholder="请输入名称" style="width:100px;margin-left:5px;margin-right:10px"></el-input>
                <el-button class="el-icon-search" @click="refreshRole">查询</el-button>
                <el-button class="el-icon-circle-plus-outline" @click="showNewRoleDialog">添加</el-button>
                <el-button class="el-icon-coin" @click="saveAuth">保存权限</el-button>
            </div>
            <el-table :data="roleList" border style="width:97%;margin-left:10px;margin-top:10px;" @row-click="roleRowClick">
                <el-table-column type="index" :index="indexMethod" label="序号" width="60"></el-table-column>
                <el-table-column prop="roleName" label="角色名称" width="120"></el-table-column>
                <el-table-column prop="note" label="角色备注"></el-table-column>
                <el-table-column label="操作" width="180">
                    <template slot-scope="scope">
                        <el-button class="el-icon-edit" @click="showEditRoleDialog(scope.row)">修改</el-button>
                        <el-button class="el-icon-delete" @click="deleteRole(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-col>
        <el-col :span="6">
            <div style="margin-top:10px;">
                <span class="font-size-12">{{ treeCheckedRoleName }}菜单访问权限</span>
            </div>
            <div class="el-tree-container">
                <el-tree ref="treeAuth" :data="treeData" :props="treeProps" show-checkbox node-key="menuId" :default-checked-keys="treeChecked"></el-tree>
            </div>
        </el-col>
        <el-dialog title="角色" :visible.sync="roleDialogVisible" width="400px">
            <el-form :model="roleForm" label-width="80px">
                <el-form-item label="角色名称">
                    <el-input v-model="roleForm.roleName" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="角色备注">
                    <el-input v-model="roleForm.note" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="roleDialogVisible=false">取消</el-button>
                <el-button type="primary" @click="saveRole">确定</el-button>
            </div>
        </el-dialog>
    </el-row>
</template>

<script>

export default {
    name: "RoleManage",
    data() {
        return {
            roleList: [],
            roleSelectRow: null,
            roleName: null,
            roleDialogVisible: false,
            roleForm: {},
            treeData: [],
            treeProps: {
                children: "childList",
                label: "menuName",
                isLeaf: "isLeaf",
                disabled: "disabled"
            },
            treeChecked: [],
            treeCheckedRoleName: null
        }
    },
    created() {
        this.refreshRole();
    },
    methods: {
        refreshRole() {
            let params = {};
            if (this.roleName != null) {
                params["roleName"] = this.roleName;
            }
            this.$http.request("/api/manage/sys/role/querySysRole", params, response => {
                this.roleList = response.list;
            });
        },
        indexMethod(index) {
            return index + 1;
        },
        showNewRoleDialog() {
            this.roleForm = {};
            this.roleDialogVisible = true;
        },
        showEditRoleDialog(row) {
            let roleId = row["roleId"];
            this.$http.request("/api/manage/sys/role/querySysRole", {roleId: roleId}, response => {
                this.roleForm = response.list[0];
                this.roleDialogVisible = true;
            });
        },
        saveRole() {
            let apiUrl = "/api/manage/sys/role/updateSysRole";
            if (this.roleForm.roleId == null) {
                apiUrl = "/api/manage/sys/role/addSysRole";
            }
            this.$confirm("您确定要保存该角色吗?", "提示").then(() => {
                this.$http.request(apiUrl, this.roleForm, response => {
                    this.refreshRole();
                    this.roleDialogVisible = false;
                });
            });
        },
        deleteRole(row) {
            let roleName = row["roleName"];
            let roleId = row["roleId"];
            this.$confirm("您确定要删除角色[" + roleName + "]吗?", "提示").then(() => {
                this.$http.request("/api/manage/sys/role/deleteSysRole", {
                    roleId: roleId
                }, response => {
                    this.refreshRole();
                });
            });
        },
        roleRowClick(row) {
            this.roleSelectRow = row;
            this.treeCheckedRoleName = row.roleName;
            this.treeData = [];
            this.treeChecked = [];
            this.refreshTree(row.roleId);
        },
        refreshTree(roleId) {
            let params = [{
                apiUrl: "/api/manage/sys/menu/querySysMenu"
            }, {
                apiUrl: "/api/manage/sys/role/querySysRoleMenu",
                roleId: roleId
            }];
            this.$http.requests(params, (response1, response2) => {
                if (response1.data.success && response1.data.list.length > 0) {
                    this.treeData = this.$util.getChildList(response1.data.list, "0", "menuId");
                }
                if (response2.data.success && response2.data.list.length > 0) {
                    let checkedId = [];
                    for (let i = 0; i < response2.data.list.length; i++) {
                        let menu = response2.data.list[i];
                        checkedId.push(menu["menuId"]);
                    }

                    this.treeChecked = checkedId;
                }
            });
        },
        saveAuth() {
            let checkedTreeNodeList = this.$refs["treeAuth"].getCheckedNodes();
            let checkedMenuId = [];
            let roleId = this.roleSelectRow.roleId;
            for (let i = 0; i < checkedTreeNodeList.length; i++) {
                let checkedTreeNode = checkedTreeNodeList[i];
                checkedMenuId.push(checkedTreeNode.menuId);
            }
            this.$http.request("/api/manage/sys/role/updateSysRoleMenu", {
                roleId: roleId,
                menuId: checkedMenuId.join(",")
            }, response => {
                this.$alert("保存角色[" + this.treeCheckedRoleName + "]的权限成功。");
            });
        }
    }
}
</script>

<style scoped lang="less">
.el-row, .el-col {
    height: 100%;
}

.el-tree-container {
    margin-top: 16px;
    width: 300px;
    height: calc(100% - 80px);
    border: 1px solid #dedede;
}
</style>