import request from '@/utils/request'

export function getSqlFileList(params) {
  return request({
    url: '/api/sysbackup',
    method: 'get',
    params: params
  })
}
export function backupDatabase() {
  return request({
    url: '/api/sysbackup',
    method: 'put'
  })
}
export function restoreDatabase(name) {
  return request({
    url: '/api/sysrestore',
    method: 'put',
    params: {
      fileName: name
    }
  })
}
export function deleteSqlFile(name) {
  return request({
    url: '/api/sysbackup',
    method: 'delete',
    params: {
      fileName: name
    }
  })
}
export function popedomrepaire() {
  return request({
    url: '/api/popedomrepaire',
    method: 'put'
  })
}
