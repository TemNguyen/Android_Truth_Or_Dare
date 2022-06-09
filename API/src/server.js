const express = require('express');
const app = express();
const morgan = require('morgan');
const admin = require('./config/firebase-config');
const db = admin.database();
const RouteApp = require('./router/index');
app.use(morgan('combined'));
app.use(express.json());

RouteApp(app);
app.post('/', (req, res) => {
    const data = req.body;
    const user = db.ref("users").push(data,(err) => {
        if(err) {
            res.send(err);
        }
        else {
            res.json({message:"success"});
        }
    })
})
app.patch('/:id', (req, res) => {
    const uid = req.params.id;
    const data = req.body;
    const user = db.ref("users").child(uid).update(data,(err) => {
        if(err) {
            res.send(err);
        }
        else {
            res.json({message:"success"});
        }
    })
})
app.get("/", (req, res) => {
    const user = db.ref("users").once("value",(snapshot) => {
        if(snapshot.val() == null){
            res.json({message:"error"});
        }
        else{
            res.json(snapshot.val());
        }
    })
})
app.get("/:id", (req, res) => {
    const uid = req.params.id;
    const user = db.ref("users").child(uid).once("value", (snapshot) => {
        if(snapshot.val() == null){
            res.json({message:"error"});
        }
        else{
            res.json(snapshot.val());
        }
    });
})
app.delete("/:id", (req, res) => {
    const uid = req.params.id;
    const user = db.ref("users").child(uid).remove((err)=>{
        if(err){
            res.send(err);
        }
        else{
            res.json({message:"success"});
        }
    })
})

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