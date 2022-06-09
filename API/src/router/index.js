const AuthRouter = require('./auth');
const QuestionRouter = require('./question');
const PackageRouter = require('./package');
const RouteApp = (app) => {
    app.use('/',AuthRouter);
    app.use('/user/question',QuestionRouter);
    app.use('/package',PackageRouter);
}
module.exports = RouteApp;