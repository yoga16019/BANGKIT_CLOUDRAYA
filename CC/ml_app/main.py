from fastapi import Depends, FastAPI, HTTPException, UploadFile, File

from . import prediction
import firebase_admin
from firebase_admin import credentials, messaging

cred = credentials.Certificate("ml_app/bangkitcr-34c18-firebase-adminsdk-91ib0-e969fae3cd.json")
firebase_admin.initialize_app(cred)

app = FastAPI()

@app.post("/v1/api/gateway/user/virtualmachines/analytics/add/{vm_id}")
def add_data(vm_id: int, new_data: float):
    return prediction.add_new_data(vm_id, new_data)

@app.post("/v1/api/gateway/user/virtualmachines/analytics/anomaly/{vm_id}")
def anomaly_detection(vm_id: int, TIME_STEPS:int, threshold:float, notirec:list, db: Session = Depends(get_db)):
    response = prediction.predict(vm_id, TIME_STEPS, threshold)
    if response == 'anomaly':
        tokens = [row[0] for row in notirec]
        notif = messaging.MulticastMessage(
            notification=messaging.Notification(
                title="An anomaly has been found in vm" + str(vm_id),
                body="We recommend you to re-check your vm with ID" + str(vm_id)
            ),
            data={
                "vm_id": str(vm_id),
                "action": "anomaly"
                },
            # Specify the token(s) of the Android device(s) to receive the notification
            tokens=tokens
        )
        responseanom = messaging.send_multicast(notif)
    return response

@app.post("/v1/api/gateway/user/virtualmachines/analytics/resource/{vm_id}")
def resource_detection(vm_id: int, recap:int, threshold:float, notirec:list, db: Session = Depends(get_db)):
    response = prediction.resource(vm_id, recap, threshold)
    if response != 'nothing' or response != 'No data has been recorded yet':
        tokens = [row[0] for row in notirec]
        notif = messaging.MulticastMessage(
            notification=messaging.Notification(
                title="You need additional resource for vm" + str(vm_id),
                body=str(response)
            ),
            data={
                "vm_id": str(vm_id),
                "action": "resource"
                },
            # Specify the token(s) of the Android device(s) to receive the notification
            tokens=tokens
        )
        responseanom = messaging.send_multicast(notif)
    return response
