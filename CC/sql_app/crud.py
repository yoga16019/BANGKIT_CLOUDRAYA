from sqlalchemy.orm import Session

import secrets

from . import models, schemas
from uuid import uuid4

def create_user(db: Session, token: str):
    new_user = models.User(token=token)
    db.add(new_user)
    db.commit()
    db.refresh(new_user)
    return new_user

def get_vm(db: Session, vm_id: int):
    return db.query(models.Vm).filter(models.Vm.id == vm_id).first()

def get_vmname(db: Session, vm_name: int):
    return db.query(models.Vm).filter(models.Vm.name == vm_name).first()

def get_vms(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.Vm).offset(skip).limit(limit).all()

def start_vm(db: Session, vm_id: int, vcode: str):
    vmver = db.query(models.Vercode).filter(models.Vercode.vm_id == vm_id and models.Vercode.action == "start").order_by(models.Vercode.id.desc()).first()
    if vmver.code == vcode:
        vm = db.query(models.Vm).filter(models.Vm.id == vm_id).first()
        vm.state = True
        db.commit()
        db.refresh(vm)
        log = db.query(models.Log).filter(models.Log.vm_id == vm_id and models.Log.action == "start").order_by(models.Log.id.desc()).first()
        log.verified = True
        db.commit()
        db.refresh(log)
        response = {"code": 200, "data": True, "message": "Please wait processing for start VM."}
    else:
        response = {"code": 404, "error": "your credential not valid", "message": "your credential not valid"}
    return response

def start_vmp(db: Session, vm_id: int):
    vmver = db.query(models.Vercode).filter(models.Vercode.vm_id == vm_id and models.Vercode.action == "start").order_by(models.Vercode.id.desc()).first()
    vm = db.query(models.Vm).filter(models.Vm.id == vm_id).first()
    vm.state = True
    db.commit()
    db.refresh(vm)
    log = db.query(models.Log).filter(models.Log.vm_id == vm_id and models.Log.action == "start").order_by(models.Log.id.desc()).first()
    log.verified = True
    db.commit()
    db.refresh(log)
    response = {"code": 200, "data": True, "message": "Please wait processing for start VM."}
    return response

def stop_vm(db: Session, vm_id: int, vcode: str):
    vmver = db.query(models.Vercode).filter(models.Vercode.vm_id == vm_id and models.Vercode.action == "stop").order_by(models.Vercode.id.desc()).first()
    if vmver.code == vcode:
        vm = db.query(models.Vm).filter(models.Vm.id == vm_id).first()
        vm.state = False
        db.commit()
        db.refresh(vm)
        log = db.query(models.Log).filter(models.Log.vm_id == vm_id and models.Log.action == "stop").order_by(models.Log.id.desc()).first()
        log.verified = True
        db.commit()
        db.refresh(log)
        response = {"code": 200, "data": True, "message": "Please wait processing for stop VM."}
    else:
        response = {"code": 404, "error": "your credential not valid", "message": "your credential not valid"}    
    return response

def stop_vmp(db: Session, vm_id: int):
    vmver = db.query(models.Vercode).filter(models.Vercode.vm_id == vm_id and models.Vercode.action == "stop").order_by(models.Vercode.id.desc()).first()
    vm = db.query(models.Vm).filter(models.Vm.id == vm_id).first()
    vm.state = False
    db.commit()
    db.refresh(vm)
    log = db.query(models.Log).filter(models.Log.vm_id == vm_id and models.Log.action == "stop").order_by(models.Log.id.desc()).first()
    log.verified = True
    db.commit()
    db.refresh(log)
    response = {"code": 200, "data": True, "message": "Please wait processing for stop VM."}
    return response

def delete_vm(db: Session, vm_id: int, vcode: str):
    vmver = db.query(models.Vercode).filter(models.Vercode.vm_id == vm_id and models.Vercode.action == "delete").order_by(models.Vercode.id.desc()).first()
    if vmver.code == vcode:
        vm = db.query(models.Vm).filter(models.Vm.id == vm_id).first()
        db.delete(vm)
        db.commit()
        db.refresh(vm)
        log = db.query(models.Log).filter(models.Log.vm_id == vm_id and models.Log.action == "delete").order_by(models.Log.id.desc()).first()
        log.verified = True
        db.commit()
        db.refresh(log)
        response = {"code": 200, "data": True, "message": "Please wait processing for delete VM."}
    else:
        response = {"code": 404, "error": "your credential not valid", "message": "your credential not valid"}
    return response

def delete_vmp(db: Session, vm_id: int, vcode: str):
    vmver = db.query(models.Vercode).filter(models.Vercode.vm_id == vm_id and models.Vercode.action == "delete").order_by(models.Vercode.id.desc()).first()
    vm = db.query(models.Vm).filter(models.Vm.id == vm_id).first()
    db.delete(vm)
    db.commit()
    db.refresh(vm)
    log = db.query(models.Log).filter(models.Log.vm_id == vm_id and models.Log.action == "delete").order_by(models.Log.id.desc()).first()
    log.verified = True
    db.commit()
    db.refresh(log)
    response = {"code": 200, "data": True, "message": "Please wait processing for delete VM."}
    return response

def get_unverified(db: Session, token: str, info: bool = False):
    vm = db.query(models.Vm).filter(models.Vm.is_active == info).order_by(models.Vm.id.desc()).first()
    if codelog:
        raise HTTPException(status_code=400, detail="no notification")
    message = messaging.Message(
        notification=messaging.Notification(
            title="We need your verification" ,
            body="New VM with ID" + str(vm.id) + "has been made"
        ),
        # Specify the token(s) of the Android device(s) to receive the notification
        token=token
    )

    # Send the notification using the Firebase Admin SDK
    response = messaging.send(message)
    return {"message": "Notification sent successfully", "vm_id": vm_id, "response":response}

def get_unverlog(db: Session, vm_id: int, info: bool = False):
    logstart = db.query(models.Log).filter(models.Log.action == "start" and models.Log.vm_id == vm_id).order_by(models.Log.id.desc()).first()
    logstop = db.query(models.Log).filter(models.Log.action == "stop" and models.Log.vm_id == vm_id).order_by(models.Log.id.desc()).first()
    logdel = db.query(models.Log).filter(models.Log.action == "delete" and models.Log.vm_id == vm_id).order_by(models.Log.id.desc()).first()
    if logstart is None:
        startres = "verified"
    elif logstart.verified == True:
        startres = "verified"
    else:
        startres = "unverified"
    
    if logstop is None:
        stopres = "verified"
    elif logstop.verified == True:
        stopres = "verified"
    else:
        stopres = "unverified"
    
    if logdel is None:
        delres = "verified"
    elif logdel.verified == True:
        delres = "verified"
    else:
        delres = "unverified"

    return {"start": startres, "stop": stopres, "delete": delres}


def create_vm(db: Session, vm: schemas.VmBase):
    new_vm = models.Vm(name=vm.name, email=vm.email)
    db.add(new_vm)
    db.commit()
    db.refresh(new_vm)
    return new_vm

def generate_random_string(length):
    characters = string.ascii_letters + string.digits
    return ''.join(secrets.choice(characters) for _ in range(length))

def create_code(db: Session, vm_id: int, action: str):
    secret_code = str(generate_random_string(6))
    new_code = models.Vercode(action=action, vm_id=vm_id, code=secret_code)
    db.add(new_code)
    db.commit()
    db.refresh(new_code)
    return new_code

def create_log(db: Session, vm_id: int, action: str):
    new_log = models.Log(action=action, vm_id=vm_id)
    db.add(new_log)
    db.commit()
    db.refresh(new_log)
    return new_log

def check_code(db: Session, vm_id: int, vcode: str):
    vmver = db.query(models.Vercode).filter(models.Vercode.vm_id == vm_id and models.Vercode.action == "create").order_by(models.Vercode.id.desc()).first()
    if vmver.code == vcode:
        vmch = db.query(models.Vm).filter(models.Vm.id == vm_id).first()
        vmch.is_active = True
        db.commit()
        db.refresh(vmch)
        response = "Success"
        logc = db.query(models.Log).filter(models.Vm.id == vm_id and models.Log.action == "create").first()
        logc.verified = True
    else:
        response = "Wrong Code"
    return response

def verify(db: Session, vm_id: int):
    vmch = db.query(models.Vm).filter(models.Vm.id == vm_id).first()
    vmch.is_active = True
    db.commit()
    db.refresh(vmch)
    return vmch