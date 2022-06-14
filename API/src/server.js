const express = require('express');
const app = express();
const morgan = require('morgan');
const RouteApp = require('./router/index');

// extra security packages
const helmet = require('helmet');
const cors = require('cors');
const xss = require('xss-clean');
const rateLimiter = require('express-rate-limit');

const swaggerUi = require('swagger-ui-express');
const swaggerDocument = require('./openapi.json');
// const YAML = require('yamljs');
// const swaggerDocument = YAML.load('swagger.yaml')

app.set('trust proxy', 1)
app.use(rateLimiter({
  windowMs: 15 * 60 * 1000,
	max: 100,
}));
app.use(helmet());
app.use(cors());
app.use(xss());

app.use(morgan('combined'))
app.use(express.json());
app.use(express.urlencoded({extended:true}));

app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocument));
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