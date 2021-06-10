
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);
const db = admin.firestore();

exports.createUser = functions.auth.user().onCreate(event => {

    const dataUser = event.data;

    
    var user = {}    
    user.uid = dataUser.uid
    user.activated = true
    user.dateCreated = Date.now()
    
    if ( dataUser.displayName ) {
        user.displayName = dataUser.displayName    
    }

    if ( dataUser.email ) {
        user.email = dataUser.email    
    }
    
    if ( dataUser.photoURL ) {
        user.photoURL = dataUser.photoURL    
    }

    var docRef = db.collection('users').doc( user.uid );

    return docRef.set( user );
});


exports.deleteUser = functions.auth.user().onDelete(event => {
    const dataUser = event.data;

    var docRef = db.collection('users').doc( dataUser.uid );

    return docRef.update({activated : false})
});


exports.imovelcreate = functions.database
.ref('/imoveis/{pushId}')
.onWrite(event => {

    var valueObject = event.data.val()
    const root      = event.data.ref.root   
    var idImovel       = event.params.pushId                    

    // Exit when the data is deleted.
    if (!event.data.exists()) {                       
        return;        
    }

    const cidade = valueObject.cidade;
    const bairro = valueObject.bairro;
    var keyCidade = "";

    var bairros = []
    bairros.push(bairro)


    return root.child('/cidades').once('value').then(objCidade => {
        if (objCidade.exists()) {

            var isExists = false
            objCidade.forEach(function(child) {                    
                
                if (child.val().cidade == cidade) {   
                    keyCidade = child.key

                    if (child.val().hasOwnProperty('bairros')) {                        
                        if (child.val().bairros instanceof Array) {                                
                            child.val().bairros.forEach(function(bair) {
                                if (bair != bairro) {
                                    bairros.push(bair)
                                }                            
                            })
                        }
                    }
                        
                    isExists = true                    
                }                                    
            });
                
            if (!isExists) {            
                return root.child('/cidades').push().set({'cidade' : cidade, 'bairros' : bairros})                          
            } else {
               return root.child('/cidades').child(keyCidade).set({'cidade' : cidade, 'bairros' : bairros})                
            }    
        } else {
            return root.child('/cidades').push().set({'cidade' : cidade, 'bairros' : bairros})            
        }
    })
                            
});



