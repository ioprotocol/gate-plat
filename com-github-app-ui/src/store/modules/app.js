import Cookies from 'js-cookie'

const app = {
  state: {
    sidebar: {
      opened: !+Cookies.get('sidebarStatus')
    },
    sideMenu: []
  },
  mutations: {
    TOGGLE_SIDEBAR: state => {
      if (state.sidebar.opened) {
        Cookies.set('sidebarStatus', 1)
      } else {
        Cookies.set('sidebarStatus', 0)
      }
      state.sidebar.opened = !state.sidebar.opened
    },
    OPEN_SIDEBAR: state => {
      Cookies.set('sidebarStatus', 0)
      state.sidebar.opened = true
    },
    CLOSE_SIDEBAR: state => {
      Cookies.set('sidebarStatus', 1)
      state.sidebar.opened = false
    },
    SET_SIDE_MENU: (state, menus) => {
      state.sideMenu = menus
    }
  },
  actions: {
    ToggleSideBar: ({ commit }) => {
      commit('TOGGLE_SIDEBAR')
    },
    OpenSidebar: ({ commit }) => {
      commit('OPEN_SIDEBAR')
    },
    CloseSidebar: ({ commit }) => {
      commit('CLOSE_SIDEBAR')
    }
  }
}

export default app
