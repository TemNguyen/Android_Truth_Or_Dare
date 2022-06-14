const QuestionRouter = require('./question');
const PackageRouter = require('./package');
const RouteApp = (app) => {
    app.use('/user',QuestionRouter);
    app.use('/package',PackageRouter);
}
module.exports = RouteApp;