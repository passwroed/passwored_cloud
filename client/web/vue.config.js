const path = require('path');
function resolve(dir) {
    return path.join(__dirname, dir)
}

module.exports = {
    publicPath: '/eparty',
    outputDir: 'eparty',
    devServer: {
        port: 8080,
        disableHostCheck: true
    },
    chainWebpack: config => {
        // 自定义路径
        config.resolve.alias
            .set('@', resolve('src'))
            .set('assets', resolve('src/assets'))
            .set('components', resolve('src/components'))
    }
};
