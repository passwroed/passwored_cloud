<template>
    <div class="adminuser">
        <div class="conditions">
            <el-button class="el-icon-search" @click="getUserList">查询</el-button>
            <el-button class="el-icon-circle-plus-outline" @click="showInsertDialog">添加</el-button>
        </div>
        <el-table :max-height="userTable.height" :data="userList" border>
            <el-table-column type="index" width="60" label="序号"></el-table-column>
            <el-table-column prop="name" label="用户名" width="100"></el-table-column>
            <el-table-column prop="phone" label="手机号" width="120"></el-table-column>
            <el-table-column prop="orgName" label="组织" width="150"></el-table-column>
            <el-table-column prop="roleName" label="角色" width="150"></el-table-column>
            <el-table-column prop="open" label="状态" width="70"></el-table-column>
            <el-table-column prop="createDate" label="创建时间" width="180">
                <template slot-scope="scope">
                    {{ $util.getDateFormat(new Date(scope.row["createDate"])) }}
                </template>
            </el-table-column>
            <el-table-column prop="id" label="操作">
                <template slot-scope="scope">
                    <el-button class="el-icon-edit" @click="showUpdateDialog(scope.row)">修改</el-button>
                    <el-button class="el-icon-delete" @click="deleteUser(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-dialog title="管理员信息" :visible.sync="status.isShowUserDialog" width="400px" @closed="closedUserDialog">
            <div>
                <el-form :model="userForm" :rules="rules" ref="userForm" label-width="80px">
                    <el-form-item label="用户名" prop="name">
                        <el-input v-model="userForm.name" placeholder="请输入用户名" maxlength="30" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="密码" prop="password">
                        <el-input v-model="userForm.password" placeholder="请输入密码" type="password" show-password maxlength="20" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="手机号" prop="phone">
                        <el-input v-model="userForm.phone" :disabled="$util.isNotBlank(userForm.id)" maxlength="11"
                                  @input="value=>{userForm.phone = this.$util.checkNumber(value)}" placeholder="请输入电话"
                                  clearable></el-input>
                    </el-form-item>
                    <el-form-item label="组织" prop="orgId" v-if="$util.isNotBlank(userForm.id)">
                        <el-select v-model="userForm.orgId" placeholder="请选择组织" clearable>
                            <el-option v-for="item in orgList" :key="item.id" :label="item.orgName" :value="item.id"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="角色" prop="roleId" v-if="$util.isNotBlank(userForm.id)">
                        <el-select v-model="userForm.roleId" placeholder="请选择角色" clearable>
                            <el-option v-for="item in roleList" :key="item.id" :label="item.roleName" :value="item.id"></el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="closedUserDialog">取 消</el-button>
                <el-button type="primary" @click="saveUser">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: "adminuser",
    data() {
        //验证电话格式是否正确
        let validatePhone = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入联系电话'));
            } else {
                if (!this.$util.isPhoneNumber(value)) {
                    callback(new Error('联系电话格式不正确'));
                }
                callback();
            }
        };
        return {
            //组织列表
            orgList: [{id: 1, orgName: "组织1"}, {id: 2, orgName: "组织2"}],
            //角色列表
            roleList: [],
            //用户列表
            userList: [],
            //用户列表搜索条件
            userTable: {},
            //状态值
            status: {
                isShowUserDialog: false
            },
            //用户表单
            userForm: {
                id: "",
                name: "",
                password: "",
                phone: "",
                orgId: "",
                roleId: "",
            },
            //用户表单验证规则
            rules: {
                phone: [
                    {required: true, message: "请输入手机号码", trigger: "blur"},
                    {required: true, validator: validatePhone, trigger: 'blur'}
                ],
                name: [
                    {required: true, message: "请输入姓名", trigger: "blur"}
                ],
                orgId: [
                    {required: true, message: "请选择组织", trigger: "blur"}
                ],
                roleId: [
                    {required: true, message: "请选择角色", trigger: "blur"}
                ],
            },
        }
    },
    created() {
        this.getRoleList();//初始化角色列表
        this.getUserList();//初始化用户列表
    },
    methods: {
        //获取角色列表
        getRoleList() {
            this.$http.request("/apiUser/admin/role/allList", {}, (data) => {
                this.roleList = data.list;
            });
        },
        //获取用户列表
        getUserList() {
            this.$http.request("/apiUser/admin/allAdminUser", this.userTable, (data) => {
                this.userList = data.list;
            });
        },
        //显示添加用户的弹窗
        showInsertDialog() {
            this.status.isShowUserDialog = true;
        },
        //关闭添加用户的弹窗
        closedUserDialog() {
            this.userForm = {};
            this.$refs.userForm.resetFields();
            this.status.isShowUserDialog = false
        },
        //显示修改用户的弹窗
        showUpdateDialog(row) {
            this.userForm = {...row};
            this.status.isShowUserDialog = true;
        },
        //删除用户
        deleteUser(row) {
            this.$confirm('您确定要删除[' + row.name + ']吗?', '提示').then(() => {
                let params = {id: row.id};
                let apiUrl = "/apiUser/admin/delById";
                this.$http.request(apiUrl, params, () => {
                    this.getUserList();
                });
            });
        },
        //保存用户
        saveUser() {
            let userForm = this.$refs.userForm;
            userForm.validate((valid) => {
                if (valid) {
                    let params = {...this.userForm};
                    let apiUrl = "";
                    if (this.$util.isBlank(params.id)) {
                        apiUrl = "/apiUser/admin/addUser";
                        if (this.$util.isBlank(params.password)) {
                            this.$message.warning("请输入您的密码");
                            return false;
                        }
                    } else {
                        apiUrl = "/apiUser/admin/editUser";
                        params.phone = "";
                        params.account = "";
                    }
                    this.$confirm("您确定要保存该用户吗?", "提示").then(() => {
                        this.$http.request(apiUrl, params, () => {
                            this.status.isShowUserDialog = false;
                            this.getUserList();
                            this.closedUserDialog();
                        });
                    });
                } else {
                    return false;
                }
            });
        },
    }
}
</script>

<style scoped type="less">
.adminuser {
    padding: 10px;
}
</style>