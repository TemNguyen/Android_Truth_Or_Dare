const admin = require('../config/firebase-config');
const db = admin.database();
const GetAllQuestion = async (req, res, next) => {
    const userId = req.body.userId;
    let questionObject = [];
    const data = await db.ref("question").once("value", snapshot => {
        if (snapshot.val() == null) {
            res.json({ message: "null" });
        }
        else {
            const questions = snapshot.val();
            const keys = Object.keys(questions);
            for (let i = 0; i < keys.length; i++) {
                if (questions[keys[i]]?.userId === userId) {
                    questionObject.push({
                        id: keys[i],
                        content: questions[keys[i]].content,
                        userId: questions[keys[i]].userId,
                        packageId: questions[keys[i]].packageId
                    })
                }
            }
        }
    })
    res.status(200).json(JSON.parse(JSON.stringify(questionObject)));
}
const GetQuestion = async (req, res, next) => {
    const id = req.params.id;
    const data = await db.ref("question").child(id).once("value", snapshot => {
        if (snapshot.val() == null) {
            res.json({ message: "null" });
        }
        else {
            const question = snapshot.val();
            const questionObject = {
                id:id,
                content: question.content,
                userId:question.userId,
                packageId:question.packageId
            };
            res.status(200).json(JSON.parse(JSON.stringify(questionObject)));
        }
    })
}
const CreateQuestion = async (req, res, next) => {
    const data = {
        userId: req.body.userId,
        content: req.body.content,
        packageId: "null"
    }
    const question = await db.ref("question").push(data);
    res.status(200).json({ 
        id: question.key,
        userId: data.userId,
        content: data.content,
        packageId: data.packageId,
    });
}
const EditQuestion = async (req, res, next) => {
    const id = req.params.id;
    const content = req.body.content;
    let questionObject;
    const data = await db.ref("question").child(id).once("value", snapshot => {
        if (snapshot.val() == null) {
            res.json({ message: "null" });
        }
        else {
            const question = snapshot.val();
            questionObject = {
                content: content,
                userId:question.userId,
                packageId:question.packageId
            };
        }
    })
    const question = db.ref("question").child(id).update(questionObject,(err) => {
        if(err) {
            res.json({ msg: "error"});
        }
        else {
            res.status(200).json({msg:"update success"});
        }
    })
}
const DeleteQuestion = async (req, res, next) => {
    const id = req.params.id;
    const question = db.ref("question").child(id).remove((err)=>{
        if(err){
            res.send(err);
        }
        else{
            res.json({message:"delete success"});
        }
    })
}
const ChooseQuestionInPackage = async (req, res, next) => {
    const id = req.params.id;
    const userId = req.body.userId;
    const packageId = req.params.packageId;
    let questionObject;
    const data = await db.ref("question").child(id).once("value", snapshot => {
        if (snapshot.val() == null) {
            res.json({ message: "null" });
        }
        else {
            const question = snapshot.val();
            questionObject = {
                content: question.content,
                userId:question.userId,
                packageId:"null"
            };
        }
    })
    const question = await db.ref("question").push(questionObject);
    res.status(200).json({ 
        id: question.key,
        userId: userId,
        content: questionObject.content,
        packageId: questionObject.packageId,
    });
}

module.exports = { GetAllQuestion, CreateQuestion, DeleteQuestion, GetQuestion, EditQuestion,ChooseQuestionInPackage }