const { ACCEPTED } = require('http-status-codes');
const authRouter = require('./auth');
const packageRouter = require('./package');
const QuestionRouter = require('./question');
const RouteApp = (app) => {
    app.use('/users',authRouter);
    app.use('/packages',packageRouter);
    app.use('/questions',QuestionRouter)
}

module.exports = RouteApp;