import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token'
const CaptchaModelKey = 'Captcha-Model'
const CaptchaLengthKey = 'Captcha-Length'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function getCaptchaModel() {
  return Cookies.get(CaptchaModelKey)
}

export function setCaptchaModel(model) {
  return Cookies.set(CaptchaModelKey, model)
}

export function getCaptchaLength() {
  return Cookies.get(CaptchaLengthKey)
}

export function setCaptchaLength(length) {
  return Cookies.set(CaptchaLengthKey, length)
}
