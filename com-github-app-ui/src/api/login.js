import request from '@/utils/request'

export function login(params) {
  return request({
    url: '/open/auth',
    method: 'post',
    data: params
  })
}
export function getCaptchaInfo() {
  return request({
    url: '/open/captcha',
    method: 'get'
  })
}

export function getInfo() {
  return request({
    url: '/api/account/current/login',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/api/account/logout',
    method: 'put'
  })
}
