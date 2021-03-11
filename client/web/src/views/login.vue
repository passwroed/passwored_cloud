<template>
    <div class="login">
        <div class="container">
            <div class="left-container">
                <div class="text-a">欢迎使用</div>
                <div class="text-b">智慧党建后台管理系统</div>
                <el-image class="img" :src="require('../assets/image/login_bg.png')" fit="fill"/>
            </div>
            <div class="right-container">
                <div class="text-a">党建管理系统</div>
                <div class="text-b">登录</div>
                <el-input class="form-input" v-model="username" placeholder="请输入账号" clearable prefix-icon="el-icon-user" size="small"></el-input>
                <el-input class="form-input" v-model="password" placeholder="请输入密码" clearable prefix-icon="el-icon-lock" show-password size="small"></el-input>
                <el-input class="form-input" v-model="authCode" placeholder="请输入验证码" clearable prefix-icon="el-icon-key" size="small"></el-input>
                <el-checkbox class="text-c" v-model="isRemember">记住密码</el-checkbox>
                <el-button class="login-button" type="primary" size="small" @click="loginClick">登录</el-button>
                <el-button class="forget-button" type="text" @click="forgetPassword">忘记密码</el-button>
            </div>
        </div>
        <el-image fit="cover" class="background-img" :src="require('../assets/image/background.png')"/>
    </div>
</template>

<script>
import {sm4} from "gm-crypt";

export default {
    name: "login",
    data() {
        return {
            username: localStorage.getItem("USERNAME") || "",
            password: localStorage.getItem("PASSWORD") || "",
            authCode: "",
            isRemember: localStorage.getItem("IS_REMEMBER") === "true"
        }
    },
    created() {
        //响应回车事件登录
        document.onkeydown = (ev) => {
            let event = ev || event;
            if (event.keyCode === 13) {
                this.loginClick();
            }
        }
    },
    methods: {
        loginClick() {
            if (this.username.length === 0) {
                this.$message({
                    showClose: true,
                    message: '用户名不能为空，请检查!',
                    type: 'warning'
                });
                return;
            }

            if (this.password.length === 0) {
                this.$message({
                    showClose: true,
                    message: '密码不能为空，请检查!',
                    type: 'warning'
                });
                return;
            }

            this.$http.request("/apiUser/admin/login", {
                username: this.username,
                password: this.password,
            }, response => {
                if (!response.success) {
                    this.$message({
                        showClose: true,
                        message: response.message,
                        type: 'warning'
                    });
                    return;
                }
                //缓存用户信息
                let user = response.list[0];
                sessionStorage.setItem("token", user.token);
                sessionStorage.setItem("username", user.username);

                //如果选择了记住密码，则保存账号密码
                if (this.isRemember) {
                    localStorage.setItem("USERNAME", this.username);
                    localStorage.setItem("PASSWORD", this.password);
                }
                localStorage.setItem("IS_REMEMBER", this.isRemember);

                //TODO 初始化数据字典
                // this.$dict.initDict(() => {
                //跳转到主页
                this.$router.push("/manage");
                // });
            });
        },
        forgetPassword() {

        }
    }
}
</script>

<style lang="less">
.login {
    .form-input {
        .el-input__inner {
            border: none !important;
            border-radius: 0 !important;
            border-bottom: 1px solid #cbcbcb !important;
        }
    }

    .text-c {
        .el-checkbox__label {
            font-size: 13px !important;
        }
    }
}
</style>

<style scoped lang="less">
.login {
    overflow: hidden;
    width: 100vw;
    height: 100vh;

    .background-img {
        z-index: -1;
        width: 100vw;
        height: 100vh;
    }

    .container {
        height: 420px;
        position: fixed;
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%);
        display: flex;

        .left-container {
            width: 500px;
            height: 100%;
            background: #f25050;
            border-radius: 5px;
            box-shadow: 0 0 5px #f25050;
            transform: translate(5px, 0);
            color: #FFFFFF;

            .text-a {
                font-size: 20px;
                margin: 40px 0 10px 40px;
            }

            .text-b {
                font-size: 13px;
                margin-left: 40px;
            }

            .img {
                margin: 60px 0 0 30px;
                width: 85%;
            }
        }

        .right-container {
            width: 320px;
            height: 100%;
            background: #ffffff;
            border-radius: 5px;
            box-shadow: 0 0 5px #f25050;
            transform: translate(-5px, 0);

            .text-a {
                font-size: 16px;
                font-weight: bold;
                color: #f25050;
                letter-spacing: 1px;
                text-align: center;
                margin-top: 40px;
            }

            .text-b {
                font-size: 14px;
                font-weight: bold;
                color: #4e4e4e;
                letter-spacing: 1px;
                margin: 30px 0 20px 30px;
            }

            .form-input {
                width: 280px;
                margin: 0 0 10px 20px;
            }

            .text-c {
                margin: 10px 0 20px 30px;
            }

            .login-button {
                background: #f25050;
                border: 1px solid #f25050;
                width: 280px;
                margin: 0 0 20px 20px;
                font-size: 14px;
                letter-spacing: 8px;
                font-weight: bold;
            }

            .forget-button {
                margin: 0 50%;
                transform: translate(-58%, 0);
            }
        }
    }
}
</style>