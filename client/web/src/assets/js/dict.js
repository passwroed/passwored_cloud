import http from './request'
import util from './util'
import {ElMessage} from 'element-plus';

const dictUrl = "/api/manage/sys/dict/getDictFromCache";

/**
 * 初始化数据字典
 */
const initDict = function (success) {
    http.request(dictUrl, {}, (data) => {
        let dictList = data.list;
        if (util.isBlank(dictList) || dictList.length == 0) {
            ElMessage.warning("初始化数据字典失败");
            return;
        }
        sessionStorage.setItem("DICT_LIST", JSON.stringify(dictList));
        if (success) {
            success();
        }
    }, () => {
        ElMessage.warning("初始化数据字典失败");
    });
};

/**
 * 根据字典代码获取数据字典子项
 */
const getDictByCode = function (dictCode) {
    let dictList = JSON.parse(sessionStorage.getItem("DICT_LIST"));
    if (util.isBlank(dictList) || dictList.length == 0) {
        ElMessage.warning("数据字典未初始化");
        return null;
    }
    for (let dict of dictList) {
        if (dict.dictCode == dictCode) {
            return dict.dictItem;
        }
    }
    ElMessage.warning("未获取到数据字典");
    return null;
};


/**
 * 根据字典代码和子项值获取数据字典子项名称
 */
const getItemNameByCodeAndVal = function (dictCode, itemVal) {
    let itemList = getDictByCode(dictCode);
    if (util.isBlank(itemList) || itemList.length == 0) {
        ElMessage.warning("数据字典未初始化");
        return null;
    }
    for (let item of itemList) {
        if (item.itemVal == itemVal) {
            return item.itemName;
        }
    }
    ElMessage.warning("未获取到数据字典");
    return null;
};

export default {
    initDict,
    getDictByCode,
    getItemNameByCodeAndVal
}