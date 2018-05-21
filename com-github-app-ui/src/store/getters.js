const getters = {
  sidebar: state => state.app.sidebar,
  token: state => state.user.token,
  name: state => state.user.name,
  roles: state => state.user.roles,
  codes: state => state.user.codes,
  captchaModel: state => state.user.captchaModel,
  captchaLength: state => state.user.captchaLength
}
export default getters
