const express = require('express');
const app = express();
const morgan = require('morgan');
const RouteApp = require('./router/index');
app.use(morgan('combined'));
app.use(express.json());

RouteApp(app);
const PORT = process.env.PORT || 3000;
const start = async () => {
    try {
        app.listen(PORT,() => {
            console.log('listening on port ' + PORT);
        });
    } catch (error) {
        console.log(error);
    }
}
start();