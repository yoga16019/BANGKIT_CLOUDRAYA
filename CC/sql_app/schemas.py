from pydantic import BaseModel, EmailStr

class VmBase(BaseModel):
    name: str
    email: EmailStr

    class Config:
        orm_mode = True

class Vm(VmBase):
    id: int
    state : bool
    is_active: bool

    class Config:
        orm_mode = True

class VercodeBase(BaseModel):
    vm_id: int
    action: str

class Vercode(VercodeBase):
    id: int
    code: str

    class Config:
        orm_mode = True