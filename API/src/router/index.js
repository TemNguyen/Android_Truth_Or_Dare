const AuthRouter = require('./auth');
const QuestionRouter = require('./question');
const RouteApp = (app) => {
    app.use('/',AuthRouter);
    app.use('/user/question',QuestionRouter);
}
module.exports = RouteApp;