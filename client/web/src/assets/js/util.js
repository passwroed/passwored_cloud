/**
 * 判断是否为空
 * @param obj
 * @returns {boolean}
 */
const isBlank = (obj) => {
    if (typeof obj === "number" && !isNaN(obj)) {
        return false;
    }
    if (!obj) {
        return true;
    }
    if (obj === "undefined" || obj === "null") {
        return true;
    }
    return Object.keys(obj).length < 1
};

/**
 * 判断是否不为空
 * @param obj
 * @returns {boolean}
 */
const isNotBlank = (obj) => {
    return !isBlank(obj)
}

/**
 * 使用@inpute时提供的校验方法 —— 校验纯数字
 * @param value
 * @returns {*}
 */
const checkNumber = value => {
    return value.replace(/[^\d]/g, '')
};

/**
 * 使用@inpute时提供的校验方法 —— 校验金额，允许两位小数
 * @param value
 */
const checkMoney = value => {
    if (value) {
        value = value.toString();
        value = value.replace(/^\D*(\d*(?:\.\d{0,2})?).*$/g, '$1');
        if (value.length >= 2 && value[0] === '0' && (value[1] === '0' || value[1] !== '.')) {
            return '0';
        }
        return value;
    }
};

/**
 * 手机号码判断
 * @param number
 * @returns {boolean}
 */
const isPhoneNumber = (number) => {
    let reg = /^(\+86)?1[3456789]\d{9}$/;
    return reg.test(number);
};

/**
 * 获取时间戳
 * @returns {number}
 */
const getTimestamp = () => {
    return new Date().getTime();
}

/**
 * 获取当前日期
 * @returns {string}
 */
const getNowDate = () => {
    return getDateFormat(new Date(), "yyyy-MM-dd");
}

/**
 * 获取当前时间
 * @returns {string}
 */
const getNowTime = () => {
    return getDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
}

/**
 * 自定义返回日期格式
 * @param date
 * @param fmt
 * @param space
 * @returns {string}
 */
const getDateFormat = function (date, fmt, space) {
    if (fmt == null) {
        fmt = "yyyy-MM-dd HH:mm:ss";
    }
    if (space == null) {
        space = "-";
    }
    let year = date.getFullYear();
    let month = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1);
    let day = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate());
    let hour = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours());
    let minutes = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes());
    let seconds = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
    let milliseconds = (date.getMilliseconds() < 10 ? '00' + date.getMilliseconds() : (date.getMilliseconds() < 100 ? "0" + date.getMilliseconds() : date.getMilliseconds()));
    let result = ""
    switch (fmt.toLowerCase()) {
        case "yyyy-mm-dd":
            result = year + space + month + space + day;
            break;
        case "yyyy-mm-dd hh:mm:ss":
            result = year + space + month + space + day + " " + hour + ":" + minutes + ":" + seconds;
            break;
        case "yyyy-mm-dd hh:mm:ss.sss":
            result = year + space + month + space + day + " " + hour + ":" + minutes + ":" + seconds + "." + milliseconds;
            break;
        default:
            result = year + space + month + space + day + " " + hour + ":" + minutes + ":" + seconds;
            break;
    }
    return result;
}

/**
 * 转换成级联数据
 * @param data
 * @param parentId
 * @param field
 * @returns {Array}
 */
const getChildList = function (data, parentId, field) {
    let childList = [];
    data.forEach(item => {
        item.id = item.id.toString();
        item.pid = item.pid.toString();
        if (parentId !== item.pid) return true;
        if (parseInt(item.type) !== 2) item.childList = getChildList(data, item[field], field);
        childList.push(item);
    });
    return childList;
};

/**
 * 获取级联选择框的初始数据
 * @param data
 * @param field
 * @returns {Array}
 */
const getCascaderData = function (data, field) {
    return data.filter(item => item.isLeaf == 1).map(item => {
        return getResult(item, data, field);
    });
};

function getResult(item, data, field) {
    let result = [item[field]];
    if (item.parentId == "0") return result;
    result.unshift(parseInt(item.parentId));
    let _this = data.filter(i => i[field] == item.parentId).map(j => {
        return j;
    });
    if (_this[0].parentId == "0") return result;
    getResult(_this, result, data);
}

export default {
    isBlank,//判断是否为空
    isNotBlank,//判断是否不为空
    checkMoney,//判断是否是金额格式
    checkNumber,//判断是否是数字
    getChildList,//转换成级联数据
    getCascaderData,//获取级联选择框的初始数据
    getTimestamp,//获取时间戳
    getNowDate,//获取当前日期
    getNowTime,//获取当前时间
    getDateFormat,//自定义返回日期格式
    isPhoneNumber,//手机号码判断
}