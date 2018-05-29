import Vue from 'vue'
import Router from 'vue-router'

// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirct in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
**/
export const constantRouterMap = [
  {
    path: '/login',
    component: () => import('@/views/login/index')
  },
  {
    path: '/404',
    component: () => import('@/views/404')
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: '系统首页',
    icon: 'home',
    code: 'vuedashboard',
    children: [{ path: 'dashboard', component: () => import('@/views/dashboard/index') }]
  },
  {
    path: '/api',
    component: Layout,
    redirect: 'noredirect',
    name: '系统管理',
    icon: 'settings',
    code: 'vuesystemmgr',
    children: [
      { path: 'account/list', name: '账号管理', code: 'vueaccountmgr', component: () => import('@/views/system/account') },
      { path: 'role/list', name: '角色管理', code: 'vuerolemgr', component: () => import('@/views/system/role') },
      { path: 'sysBackup', name: '数据备份', code: 'vuedbback', component: () => import('@/views/system/databack') },
      { path: 'log/list', name: '日志管理', code: 'vuelogmgr', component: () => import('@/views/system/log') },
      { path: 'account/onlinestat', name: '在线用户', code: 'vueonlineaccount', component: () => import('@/views/system/session') },
      { path: 'system/performance', name: '性能监控', code: 'vueperformance', component: () => import('@/views/system/performance') }
    ]
  },
  {
    path: '*',
    redirect: '/404'
  }
]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

