// Simple in-memory auth + todo server for local development
// Run: node server.js
const http = require('http')

// Minimal UUID generator
function uuid() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8)
    return v.toString(16)
  })
}

const users = {} // username -> { password, refreshToken, accessToken }
const todos = {} // username -> [{ id, text }]

function parseJson(req) {
  return new Promise((res, rej) => {
    let body = ''
    req.on('data', chunk => body += chunk)
    req.on('end', () => {
      try { res(JSON.parse(body || '{}')) } catch(e) { res({}) }
    })
  })
}

function send(res, status, obj) {
  const payload = JSON.stringify(obj)
  res.writeHead(status, { 'Content-Type': 'application/json' })
  res.end(payload)
}

function tokenForUser(username) {
  const access = uuid()
  const refresh = uuid()
  users[username].accessToken = access
  users[username].refreshToken = refresh
  return { access_token: access, refresh_token: refresh }
}

const server = http.createServer(async (req, res) => {
  const url = req.url
  if (req.method === 'POST' && url === '/register') {
    const body = await parseJson(req)
    const { username, password } = body
    if (!username || !password) return send(res, 400, { error: 'username/password required' })
    if (users[username]) return send(res, 400, { error: 'user exists' })
    users[username] = { password }
    todos[username] = []
    const tokens = tokenForUser(username)
    return send(res, 200, tokens)
  }

  if (req.method === 'POST' && url === '/login') {
    const body = await parseJson(req)
    const { username, password } = body
    const user = users[username]
    if (!user || user.password !== password) return send(res, 401, { error: 'invalid credentials' })
    const tokens = tokenForUser(username)
    return send(res, 200, tokens)
  }

  if (req.method === 'POST' && url === '/refresh') {
    const auth = req.headers['authorization'] || ''
    const refresh = auth.replace('Bearer ', '')
    const entry = Object.entries(users).find(([k,v]) => v.refreshToken === refresh)
    if (!entry) return send(res, 401, { error: 'invalid refresh' })
    const username = entry[0]
    const tokens = tokenForUser(username)
    return send(res, 200, tokens)
  }

  // require bearer access token
  const auth = req.headers['authorization'] || ''
  const access = auth.replace('Bearer ', '')
  const entry = Object.entries(users).find(([k,v]) => v.accessToken === access)
  if (!entry) {
    return send(res, 401, { error: 'unauthorized' })
  }
  const username = entry[0]

  if (req.method === 'GET' && url === '/items') {
    return send(res, 200, todos[username] || [])
  }

  if (req.method === 'POST' && url === '/items/add') {
    const body = await parseJson(req)
    const text = body.text
    if (!text) return send(res, 400, { error: 'text required' })
    const id = Math.floor(Math.random() * 1000000)
    const item = { id, text }
    todos[username] = todos[username] || []
    todos[username].push(item)
    return send(res, 200, { ok: true })
  }

  send(res, 404, { error: 'not found' })
})

const port = 8080
server.listen(port, () => console.log('Server running on http://localhost:' + port))
