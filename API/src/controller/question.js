const admin = require('../config/firebase-config');
const db = admin.database();
const GetAllQuestion = async (req, res, next) => {
    const userId = req.params.userId;
    let questionObject = [];
    const data = await db.ref("question").once("value", snapshot => {
        if (snapshot.val() == null) {
            res.status(404).json({ message: "null" });
        }
        else {
            const questions = snapshot.val();
            const keys = Object.keys(questions);
            for (let i = 0; i < keys.length; i++) {
                if (questions[keys[i]].userId === userId && questions[keys[i]].packageId === "null") {
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
    const userId = req.params.userId;
    const id_question = req.params.id_question;
    const data = await db.ref("question").child(id_question).once("value", snapshot => {
        if (snapshot.val() == null) {
            res.status(404).json({ message: "null" });
        }
        else {
            const question = snapshot.val();
            const questionObject = {
                id:id_question,
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
        userId: req.params.userId,
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
    const userId = req.params.userId;
    const id_question = req.params.id_question;
    const content = req.body.content;
    let questionObject;
    const data = await db.ref("question").child(id_question).once("value", snapshot => {
        if (snapshot.val() == null) {
            res.status(404).json({ message: "null" });
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
    const question = db.ref("question").child(id_question).update(questionObject,(err) => {
        if(err) {
            res.status(500).json({ msg: "error"});
        }
        else {
            res.status(200).json({msg:"update success"});
        }
    })
}
const DeleteQuestion = async (req, res, next) => {
    const userId = req.params.userId;
    const id_question = req.params.id_question;
    const question = db.ref("question").child(id_question).remove((err)=>{
        if(err){
            res.status(404).send(err);
        }
        else{
            res.json({message:"delete success"});
        }
    })
}
const ChooseQuestionInPackage = async (req, res, next) => {
    const id_question = req.params.id_question;
    const userId = req.params.userId;
    let questionObject;
    const data = await db.ref("question").child(id_question).once("value", snapshot => {
        if (snapshot.val() == null) {
            res.status(404).json({ message: "null" });
        }
        else {
            const question = snapshot.val();
            questionObject = {
                content: question.content,
                userId:userId,
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
const InstallPackage = async (req, res, next) => {
    const {userId, packageId} = req.params;
    db.ref("install").push({ 
        userId: userId,
        packageId: packageId
    })
    res.status(200).json({message: "install success"});
}
const GetPackageInstalled = async (req, res, next) => {
    const {userId} = req.params;
    let packageObject = [];
    const package = await db.ref("install").once("value", snapshot => {
        if (snapshot.val() == null) {
            res.status(404).json({ message: "null" });
        }
        else {
            const packages = snapshot.val();
            const keys = Object.keys(packages);
            for (let i = 0; i < keys.length; i++) {
                if (packages[keys[i]].userId === userId) {
                    packageObject.push({
                        userId: packages[keys[i]].userId,
                        packageId: packages[keys[i]].packageId
                    })
                }
            }
        }
    })
    console.log(packageObject);
    res.status(200).json(JSON.parse(JSON.stringify(packageObject)));
}

module.exports = { GetAllQuestion, CreateQuestion, DeleteQuestion, GetQuestion, EditQuestion,ChooseQuestionInPackage,InstallPackage,GetPackageInstalled }