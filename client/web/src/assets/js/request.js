import axios from 'axios';
import {ElMessage} from 'element-plus';
import {sm4} from "gm-crypt"
import util from "util"

// TODO 接口统一访问地址
// const server = "http://" + window.location.host.split(":")[0] + ":9000";
const server = "http://localhost:9000";
// const server = "http://192.168.124.29:9000";
const uploadFile = server + "/api/manage/sys/file/uploadFile";
const getFile = server + "/api/manage/sys/file/getFile";

const request = function (apiUrl, params, success, fail) {
    let token = sessionStorage.getItem("token");
    if (util.isBlank(token) || apiUrl === "/apiUser/admin/login") {
        token = "";
    }

    // 初始化请求参数
    if (util.isBlank(params)) {
        params = {};
    }

    //TODO 对参数进行加密

    //配置sm4参数
    // let sm4Config = {
    //     key: "5efsYqfySlfzPb7L",
    //     mode: "ecb",
    //     iv: null,
    //     cipherType: "base64"
    // };
    //
    // //初始化sm4对象
    // let SM4 = new sm4(sm4Config);
    //
    // //对参数进行加密
    // let paramsStr = "";
    // try {
    //     paramsStr = JSON.stringify(params);
    //     params = {data: SM4.encrypt(paramsStr)}
    // } catch (e) {
    //     Message.warning("请求参数错误：" + e);
    //     return;
    // }

    // 进行异步请求
    axios({
        url: server + apiUrl,
        method: "post",
        headers: {
            token: token
        },
        data: params
    }).then(response => {
        let result = response.data;

        //TODO 对参数进行解密

        switch (result.code) {
            case 200:
                if (success) {
                    success(result);
                }
                break;
            case 400:
                ElMessage.warning("请求语法错误，原因是:" + result.message);
                break;
            case 401:
                ElMessage.warning("无访问权限，原因是:" + result.message);
                break;
            case 403:
                ElMessage.warning("服务器拒绝请求，原因是:" + result.message);
                break;
            case 404:
                ElMessage.warning("请求地址不存在，原因是:" + result.message);
                break;
            case 500:
                ElMessage.warning("服务器内部错误，原因是:" + result.message);
                break;
            case 5001:
                ElMessage.warning("常规服务访问错误，原因是:" + result.message);
                break;
            case 5002:
                ElMessage.warning("Feign访问错误，原因是:" + result.message);
                break;
            case 4001:
            case 4002:
            case 4003:
            case 4004:
                ElMessage.warning("token访问错误，原因是:" + result.message);
                break;
            default:
                ElMessage.warning("其他错误，原因是:" + result.message);
                if (fail) {
                    fail(result.message);
                }
                break;
        }
    }).catch(function (error) {
        ElMessage.warning("内部错误，原因是:" + error);
        if (fail) {
            fail(error);
        }
    })
};

const requests = function (params, complete) {
    let token = sessionStorage.getItem("token");
    let list = [];
    for (let i = 0; i < params.length; i++) {
        let param = params[i];
        let apiUrl = param.apiUrl;
        delete param.apiUrl;
        list.push(axios({
            url: server + apiUrl,
            method: "post",
            headers: {
                token: token
            },
            data: param
        }));
    }
    axios.all(list).then(axios.spread(complete));
};

export default {
    uploadFile,
    getFile,
    request,
    requests
}