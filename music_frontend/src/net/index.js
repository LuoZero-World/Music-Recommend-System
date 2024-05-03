import axios from "axios"
import {ElMessage} from "element-plus";

const defaultError = () => ElMessage.error('发生了一些错误')
const defaultFailure = (message) =>ElMessage.warning(message)

function post(url, data, success, failure=defaultFailure, error=defaultError){
    axios.post(url, data, {
        headers:{
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        withCredentials: true
    }).then(({data}) => {
        // console.log(data)
        if(data.code === 200)
            success(data.message, data.data)
        else failure(data.message)
    }).catch(error)
}

function get(url, success, failure=defaultFailure, error=defaultError){
    axios.get(url, {
        withCredentials: true
    }).then(({data}) => {
        // console.log(data)
        if(data.code === 200)
            success(data.message, data.data)
        else failure(data.message)
    }).catch(error)
}

async function postFile(url, data, success, failure = defaultFailure, error = defaultError) {
    try {
        const response = await axios.post(url, data, {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            responseType: 'blob',
            withCredentials: true
        });
        success(response);
    } catch (err) {
        error(err); // 假设 error 是一个全局定义的错误处理函数
    }
}

export {get, post, postFile}
