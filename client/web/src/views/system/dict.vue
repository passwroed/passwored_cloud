<template>
    <el-row>
        <el-col :span="12">
            <div style="margin-left:10px;margin-top:10px;">
                <span class="font-size-12">名称:</span>
                <el-input v-model="dictName" placeholder="请输入名称" style="width:100px;margin-left:5px;margin-right:10px"></el-input>
                <span class="font-size-12">代码:</span>
                <el-input v-model="dictCode" placeholder="请输入代码" style="width:100px;margin-left:5px;margin-right:10px"></el-input>
                <el-button class="el-icon-search" @click="refreshDict">查询</el-button>
                <el-button class="el-icon-circle-plus-outline" @click="showNewDictDialog">添加</el-button>
                <el-button class="el-icon-upload2" @click="refreshDictCache">同步数据字典到缓存</el-button>
            </div>
            <el-table :data="dictList" :height="tableHeight" border :highlight-current-row="true" style="width:97%;margin-left:10px;margin-top:10px;"
                      @row-click="roleRowClick">
                <el-table-column type="index" :index="indexMethod" label="序号" width="60"></el-table-column>
                <el-table-column prop="dictName" label="字典名称"></el-table-column>
                <el-table-column prop="dictCode" label="字典代码"></el-table-column>
                <el-table-column prop="dictNote" label="字典备注"></el-table-column>
                <el-table-column label="操作" width="180">
                    <template slot-scope="scope">
                        <el-button class="el-icon-edit" @click="showEditDictDialog(scope.row)">修改</el-button>
                        <el-button class="el-icon-delete" @click="deleteDict(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-col>
        <el-col :span="12">
            <div style="margin-top:10px;">
                <span class="font-size-12">名称:</span>
                <el-input v-model="dictItemName" placeholder="请输入名称" style="width:100px;margin-left:5px;margin-right:10px"></el-input>
                <el-button class="el-icon-search" @click="refreshDictItem" :disabled="dictListSelectedRow==null">查询</el-button>
                <el-button class="el-icon-circle-plus-outline" @click="showNewDictItemDialog" :disabled="dictListSelectedRow==null">添加</el-button>
            </div>
            <el-table :data="dictItemList" :height="tableHeight" border style="width:97%;margin-top:10px;">
                <el-table-column type="index" :index="indexMethod" label="序号" width="60"></el-table-column>
                <el-table-column prop="itemName" label="子项名称"></el-table-column>
                <el-table-column prop="itemVal" label="子项值"></el-table-column>
                <el-table-column prop="itemNote" label="备注"></el-table-column>
                <el-table-column prop="itemSort" label="排序" width="80"></el-table-column>
                <el-table-column label="操作" width="180">
                    <template slot-scope="scope">
                        <el-button class="el-icon-edit" @click="showEditDictItemDialog(scope.row)">修改</el-button>
                        <el-button class="el-icon-delete" @click="deleteDictItem(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-col>
        <el-dialog title="数据字典" :visible.sync="dictDialogVisible" width="400px">
            <el-form :model="dictForm" label-width="80px">
                <el-form-item label="字典名称">
                    <el-input v-model="dictForm.dictName" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="字典代码">
                    <el-input v-model="dictForm.dictCode" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="dictForm.dictNote" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dictDialogVisible=false">取消</el-button>
                <el-button type="primary" @click="saveDict">确定</el-button>
            </div>
        </el-dialog>
        <el-dialog title="数据字典子项" :visible.sync="dictItemDialogVisible" width="400px">
            <el-form :model="dictItemForm" label-width="80px">
                <el-form-item label="子项名称">
                    <el-input v-model="dictItemForm.itemName" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="子项值">
                    <el-input v-model="dictItemForm.itemVal" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="排序">
                    <el-input v-model="dictItemForm.itemSort" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="dictItemForm.itemNote" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dictItemDialogVisible=false">取消</el-button>
                <el-button type="primary" @click="saveDictItem">确定</el-button>
            </div>
        </el-dialog>
    </el-row>
</template>

<script>
    export default {
        name: "Dict",
        data() {
            return {
                tableHeight: 0,
                dictName: null,
                dictCode: null,
                dictList: [],
                dictListSelectedRow: null,
                dictForm: {},
                dictDialogVisible: false,
                dictItemName: null,
                dictItemList: [],
                dictItemForm: {},
                dictItemDialogVisible: false
            }
        },
        created() {
            this.tableHeight = document.body.clientHeight - 180;
            this.refreshDict();
        },
        methods: {
            refreshDict() {
                let params = {};
                if (this.dictName != null && this.dictName.length > 0) {
                    params["dictName"] = this.dictName;
                }
                if (this.dictCode != null && this.dictCode.length > 0) {
                    params["dictCode"] = this.dictCode;
                }
                this.$http.request("/api/manage/sys/dict/querySysDict", params, response => {
                    this.dictList = response.list;
                    this.dictListSelectedRow = null;
                    this.dictItemList = [];
                });
            },
            refreshDictItem() {
                let params = {
                    dictId: this.dictListSelectedRow.dictId
                };
                if (this.dictItemName != null && this.dictItemName.length > 0) {
                    params["dictItemName"] = this.dictItemName;
                }
                this.$http.request("/api/manage/sys/dict/querySysDictItem", params, response => {
                    this.dictItemList = response.list;
                });
            },
            refreshDictCache() {
                this.$http.request("/api/manage/sys/dict/refreshDict", {}, response => {
                    let dictList = response.list;
                    if (this.$util.isBlank(dictList)) {
                        this.$message({showClose: true, message: "同步失败", type: 'warning'});
                        return;
                    }
                    sessionStorage.setItem("DICT_LIST", JSON.stringify(dictList));
                    this.$message.success("同步成功");
                });
            },
            indexMethod(index) {
                return index + 1;
            },
            showNewDictDialog() {
                this.dictForm = {};
                this.dictDialogVisible = true;
            },
            showEditDictDialog(row) {
                let dictId = row["dictId"];
                this.$http.request("/api/manage/sys/dict/querySysDict", {dictId: dictId}, response => {
                    this.dictForm = response.list[0];
                    this.dictDialogVisible = true;
                });
            },
            saveDict() {
                let apiUrl = "/api/manage/sys/dict/updateSysDict";
                if (this.dictForm.dictId == null) {
                    apiUrl = "/api/manage/sys/dict/addSysDict";
                }
                this.$confirm("您确定要保存该数据字典吗?", "提示").then(() => {
                    this.$http.request(apiUrl, this.dictForm, () => {
                        this.refreshDict();
                        this.dictDialogVisible = false;
                    });
                });
            },
            deleteDict(row) {
                let dictName = row["dictName"];
                let dictId = row["dictId"];
                this.$confirm("您确定要删除数据字典[" + dictName + "]吗?", "提示").then(() => {
                    this.$http.request("/api/manage/sys/dict/deleteSysDict", {
                        dictId: dictId
                    }, () => {
                        this.refreshDict();
                    });
                });
            },
            showNewDictItemDialog() {
                this.dictItemForm = {};
                this.dictItemDialogVisible = true;
            },
            showEditDictItemDialog(row) {
                let itemId = row["itemId"];
                this.$http.request("/api/manage/sys/dict/querySysDictItem", {itemId: itemId}, response => {
                    this.dictItemForm = response.list[0];
                    this.dictItemDialogVisible = true;
                });
            },
            saveDictItem() {
                let apiUrl = "/api/manage/sys/dict/updateSysDictItem";
                if (this.dictItemForm.itemId == null) {
                    apiUrl = "/api/manage/sys/dict/addSysDictItem";
                    this.dictItemForm.dictId = this.dictListSelectedRow.dictId;
                }
                this.$confirm("您确定要保存该数据字典子项吗?", "提示").then(() => {
                    this.$http.request(apiUrl, this.dictItemForm, () => {
                        this.refreshDictItem();
                        this.dictItemDialogVisible = false;
                    });
                });
            },
            deleteDictItem(row) {
                let itemName = row["itemName"];
                let itemId = row["itemId"];
                this.$confirm("您确定要删除数据字典子项[" + itemName + "]吗?", "提示").then(() => {
                    this.$http.request("/api/manage/sys/dict/deleteSysDictItem", {
                        itemId: itemId
                    }, () => {
                        this.refreshDictItem();
                    });
                });
            },
            roleRowClick(row) {
                this.dictListSelectedRow = row;
                this.refreshDictItem();
            }
        }
    }
</script>

<style scoped>

</style>