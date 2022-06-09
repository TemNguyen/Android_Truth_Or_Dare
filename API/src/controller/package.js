const admin = require('../config/firebase-config');
const db = admin.database();
const GetAllPackages = async (req, res, next) => {
    let packageObject = [];
    const package = await db.ref("package").once("value", (snapshot) => {
        if (snapshot.val() == null) {
            res.json({ message: "null" });
        }
        else {
            const packages = snapshot.val();
            const keys = Object.keys(packages);
            for (let i = 0; i < keys.length; i++) {
                packageObject.push({
                    id: keys[i],
                    name: packages[keys[i]].name,
                })
            }
        }
    })
    res.status(200).json(JSON.parse(JSON.stringify(packageObject)));
}
const GetPackage = async (req, res, next) => {
    const id = req.params.id;
    const data = await db.ref("package").child(id).once("value", snapshot => {
        if (snapshot.val() == null) {
            res.json({ message: "null" });
        }
        else {
            const package = snapshot.val();
            const packageObject = {
                id:id,
                name: package.name,
            };
            res.status(200).json(JSON.parse(JSON.stringify(packageObject)));
        }
    })
}
const GetQuestionInPackage = async (req, res, next) => {
    const packageId = req.params.id;
    let questionObject = [];
    const data = await db.ref("question").once("value", snapshot => {
        if (snapshot.val() == null) {
            res.json({ message: "null" });
        }
        else {
            const questions = snapshot.val();
            const keys = Object.keys(questions);
            for (let i = 0; i < keys.length; i++) {
                if (questions[keys[i]].packageId === packageId) {
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
const CreateQuestionForPackage = async (req, res, next) => {
    const data = {
        userId: "null",
        content: req.body.content,
        packageId: req.params.id
    }
    const question = await db.ref("question").push(data);
    res.status(200).json({ 
        id: question.key,
        userId: data.userId,
        content: data.content,
        packageId: data.packageId
    });
}
const CreatePackage = async (req, res, next) => {
    const data = {
        name: req.body.name
    }
    const package = await db.ref("package").push(data);
    res.status(200).json({
        id: package.key,
        name: data.name
    });
}
const EditPackage = async (req, res, next) => {
    const id = req.params.id;
    const name = req.body.name;
    let packageObject;
    const data = await db.ref("package").child(id).once("value", snapshot => {
        if (snapshot.val() == null) {
            res.json({ message: "null" });
        }
        else {
            const package = snapshot.val();
            packageObject = {
                name: name,
            };
        }
    })
    const package = db.ref("package").child(id).update(packageObject,(err) => {
        if(err) {
            res.json({ msg: "error"});
        }
        else {
            res.status(200).json({msg:"update success"});
        }
    })
}
const DeletePackage = async (req, res, next) => {
    const id = req.params.id;
    const package = db.ref("package").child(id).remove();
    const data = await db.ref("question").once("value", snapshot => {
        if (snapshot.val() == null) {
            res.json({ message: "null" });
        }
        else {
            const questions = snapshot.val();
            const keys = Object.keys(questions);
            for (let i = 0; i < keys.length; i++) {
                if (questions[keys[i]].packageId === id) {
                    const question = db.ref("question").child(keys[i]).remove();
                }
            }
        }
    })
    res.status(200).json({ message: "delete success" });
}

module.exports = { GetAllPackages, GetPackage, GetQuestionInPackage, CreateQuestionForPackage, CreatePackage, EditPackage, DeletePackage }