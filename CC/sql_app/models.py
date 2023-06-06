from sqlalchemy import Boolean, Column, ForeignKey, Integer, String
from sqlalchemy.orm import relationship

from .database import Base


class Vm(Base):
    __tablename__ = "vms"
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, unique=True, index=True)
    email = Column(String, unique=True, index=True)
    state = Column(Boolean, default=False)
    is_active = Column(Boolean, default=False)

    codes = relationship("Vercode", back_populates="vms")
    logs = relationship("Log", back_populates="vms")

class User(Base):
    __tablename__ = "users"
    id = Column(Integer, primary_key=True, index=True)
    token = Column(String, unique=True, index=True)

class Vercode(Base):
    __tablename__ = "codes"
    id = Column(Integer, primary_key=True, index=True)
    action = Column(String)
    logs = relationship("Log", back_populates="codes")

    vm_id = Column(Integer, ForeignKey("vms.id"))
    vms = relationship("Vm", back_populates="codes")
    
    code = Column(String, index=True)

class Log(Base):
    __tablename__ = "logs"
    id = Column(Integer, primary_key=True, index=True)

    action = Column(String, ForeignKey("codes.action"))
    codes = relationship("Vercode", back_populates="logs")

    vm_id = Column(Integer, ForeignKey("vms.id"))
    vms = relationship("Vm", back_populates="logs")
    
    verified = Column(Boolean, default=False)