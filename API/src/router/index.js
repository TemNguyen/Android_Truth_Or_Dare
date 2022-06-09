const AuthRouter = require('./auth');

const RouteApp = (app) => {
    app.use('/user',AuthRouter);
}

module.exports = RouteApp;