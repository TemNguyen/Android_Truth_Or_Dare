require('dotenv').config();
require('express-async-errors');
const express = require('express');
const app = express();
const PORT = process.env.PORT || 3000;
const connectDB = require('./db/connect');
const routerApp = require('./router/index');
const morgan = require('morgan');
const session = require('express-session');
const notFoundMiddleware = require('./middleware/not-found');
const errorHandlerMiddleware = require('./middleware/error-handler');

app.use(session({ secret: 'melody hensley is my spirit animal' }));
app.use(morgan('combined'));
app.use(express.urlencoded({extended: true}));
app.use(express.json());

routerApp(app);
app.use(notFoundMiddleware);
app.use(errorHandlerMiddleware);

const start = async () => {
    try {
        await connectDB(process.env.MONGO_URI);
        app.listen(PORT,() => {
            console.log('listening on port ' +PORT);
        });
    } catch (error) {
        console.log(error);
    }
}

start();