from fastapi import Depends, FastAPI, HTTPException
from sqlalchemy.orm import Session

from . import crud, models, schemas
from .database import SessionLocal, engine

from fastapi_mail import FastMail, MessageSchema,ConnectionConfig
from starlette.requests import Request
from starlette.responses import JSONResponse
from pydantic import EmailStr, BaseModel
from typing import List

import firebase_admin
from firebase_admin import credentials, messaging

cred = credentials.Certificate("sql_app/bangkitcr-34c18-firebase-adminsdk-91ib0-e969fae3cd.json")
firebase_admin.initialize_app(cred)

models.Base.metadata.create_all(bind=engine)

app = FastAPI()

class EmailSchema(BaseModel):
   email: List[EmailStr]

conf = ConnectionConfig(
    MAIL_USERNAME ="03982692f7487a",
    MAIL_PASSWORD = "70bbc2298f66da",
    MAIL_FROM = "crone@cloudraya.com",
    MAIL_PORT = 587,
    MAIL_SERVER = "sandbox.smtp.mailtrap.io",
    MAIL_STARTTLS = True,
    MAIL_SSL_TLS = False,
    USE_CREDENTIALS = True,
    VALIDATE_CERTS = True
)


# Dependency
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@app.post("/v1/api/gateway/user/")
def create_newuser(token: str, db: Session = Depends(get_db)):
    return crud.create_user(db, token=token)

@app.post("/v1/api/gateway/user/virtualmachines", response_model=schemas.Vm)
def create_vm(vm: schemas.VmBase, db: Session = Depends(get_db)):
    db_user = crud.get_vmname(db, vm_name=vm.name)
    if db_user:
        raise HTTPException(status_code=400, detail="vm name already registered")
    return crud.create_vm(db=db, vm=vm)


@app.get("/v1/api/gateway/user/virtualmachines")
def read_vms(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    users = crud.get_vms(db, skip=skip, limit=limit)
    return {"code": 200, "data":users, "message": "Success get list of vm."}

@app.get("/v1/api/gateway/user/virtualmachines/{vm_id}")
def read_vm(vm_id: int, db: Session = Depends(get_db)):
    db_user = crud.get_vm(db, vm_id=vm_id)
    if db_user is None:
        raise HTTPException(status_code=404, detail="vm not found")
    return {"code": 200, "data":db_user, "message": "Success get list of vm."}

"""
@app.get("/v1/api/gateway/user/virtualmachines/unverified/")
def read_unverified(token: str, info: bool = False, db: Session = Depends(get_db)):
    users = crud.get_unverified(db, token=token, info=info)
    return users """

@app.get("/v1/api/gateway/user/virtualmachines/unverifiedlog/")
def read_unverifiedlog(vm_id: int, info: bool = False, db: Session = Depends(get_db)):
    users = crud.get_unverlog(db, info=info, vm_id = vm_id)
    return users

'''
@app.post("/getmail/", response_model=schemas.Vercode)
def create_code(vcode: schemas.VercodeBase, db: Session = Depends(get_db)):
    code = crud.create_code(db, vcode=vcode)
    return code
'''

@app.post("/v1/api/gateway/user/virtualmachines/getmail/{id}/{action}") #action = create, start, stop, delete
async def create_code(id: int, action: str, db: Session = Depends(get_db)):
    code = crud.create_code(db, vm_id=id, action=action)
    email = crud.get_vm(db, vm_id=id).email
    rec = [email]
    yk = str(code.code)
    template = """
        <html>
        <body>
	    <p> Here is your verification code:
        <br></p> {{code.code}}
        </body>
        </html>
        """
    log = crud.create_log(db, vm_id=id, action=action)
    message = MessageSchema(
        subject="Verification",
        recipients=rec,  # List of recipients, as many as you can pass
        body="kode verifikasi anda untuk "+ action + " VM dengan id "+ str(id) + ":" + yk,
        subtype="plain"
        )
    fm = FastMail(conf)
    await fm.send_message(message)
    #send notification
    notirec = db.query(User.token).all()
    tokens = [row[0] for row in result]
    notif = messaging.Message(
        notification=messaging.Notification(
            title="We need your verification" ,
            body="VM with ID " + str(id) + " has been " + action + "d/ed, check your mail"
        ),
        data={
            "vm_id": id, 
            "action": action
            },
        # Specify the token(s) of the Android device(s) to receive the notification
        token=tokens
    )
    response = messaging.send(notif)
    return JSONResponse(status_code=200, content={"message": "email has been sent"})

@app.post("/v1/api/gateway/user/virtualmachines/verifynewvm/{vm_id}")
def verify_vm(vm_id: int, vcode: str, db: Session = Depends(get_db)):
    db_user = crud.get_vm(db, vm_id=vm_id)
    if db_user is None:
        raise HTTPException(status_code=404, detail="vm not found")
    return crud.check_code(db, vm_id=vm_id, vcode=vcode)

'''@app.post("/verify/{vm_id}", response_model=schemas.Vm)
def verify_vm(vm_id: int, db: Session = Depends(get_db)):
    db_user = crud.get_vm(db, vm_id=vm_id)
    if db_user is None:
        raise HTTPException(status_code=404, detail="vm not found")
    return crud.verify(db, vm_id=vm_id)'''

@app.post("/v1/api/gateway/user/virtualmachines/actionpc")
def start_stop_vm(vm_id: int, request:str, vcode:str, release_ip: bool = False, db: Session = Depends(get_db)):
    vmc = db.query(models.Vm).filter(models.Vm.id == vm_id).first()
    if request == "start" and vmc.is_active == True:
        crud.start_vm(db, vm_id=vm_id, vcode=vcode)
    elif request == "stop" and vmc.is_active == True:
        crud.stop_vm(db, vm_id=vm_id, vcode=vcode)
    elif request == "destroy" and vmc.is_active == True:
        crud.delete_vm(db, vm_id=vm_id, vcode=vcode)
    else:
        return {"code": 404, "error": "your credential not valid", "message": "your credential not valid"}
    return {"code": 200, "data": true, "message": "Please wait processing for start VM."}

@app.post("/v1/api/gateway/user/virtualmachines/action")
def start_stop_vm_phone(vm_id: int, request:str, release_ip: bool = False, db: Session = Depends(get_db)):
    vmc = db.query(models.Vm).filter(models.Vm.id == vm_id).first()
    if request == "start" and vmc.is_active == True:
        crud.start_vmp(db, vm_id=vm_id)
    elif request == "stop" and vmc.is_active == True:
        crud.stop_vmp(db, vm_id=vm_id)
    elif request == "destroy" and vmc.is_active == True:
        crud.delete_vmp(db, vm_id=vm_id)
    else:
        return {"code": 404, "error": "your credential not valid", "message": "your credential not valid"}
    return {"code": 200, "data": true, "message": "Please wait processing for start VM."}