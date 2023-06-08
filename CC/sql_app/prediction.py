import pandas as pd
import numpy as np
import time
import json
import os

from google.oauth2 import service_account
from google.cloud import aiplatform
from google.protobuf import json_format

credentials = service_account.Credentials.from_service_account_file("sql_app/cloudhariraya-9394ad173f24.json")
project = "352377993515" 
location = "asia-southeast1"
aiplatform.init(project=project, location=location, credentials=credentials)
endpoint_id = "1805644383412813824"
endpoint = aiplatform.Endpoint(endpoint_id)

def convert_json_dataframe(cpu_used):
  return pd.DataFrame(cpu_used)

def add_new_data(vm_id: int, new_data: float):
    file_path = os.path.join('sql_app/cpu_data', str(vm_id) + ".json")
    if os.path.exists(file_path):
        with open(file_path, 'r') as file:
            cpu_used = json.load(file)
        dic_key = next(iter(cpu_used))
        cpu_used[dic_key].append(new_data)
        with open(file_path, 'w') as file:
            json.dump(cpu_used, file)
    else:
        data_append = {'cpu_usage': [new_data]}
        with open(file_path, 'w') as file:
            json.dump(data_append, file)
        cpu_used = data_append
    return cpu_used

def create_dataset(X,y,time_steps=1):
    Xs,ys  = [],[]
    for i in range(len(X) - time_steps):
        v = X.iloc[i:(i + time_steps)].values
        Xs.append(v)
    return np.array(Xs)

"""
def model_pred(model,data_2d,TIME_STEPS):
  return model.predict(data_2d.reshape(1,TIME_STEPS,1))
"""

def train_scaler(data,mean=45.47328475262359,std=31.79330742742731):
    data=data.copy()
    data.cpu_usage=(data.cpu_usage-mean)/std
    return data

def inverse_train_scaler(data,mean=45.47328475262359,std=31.79330742742731):
    data.cpu_usage=data.cpu_usage*std+mean
    return data.cpu_usage

def inverse_train_scaler_array(data, mean=45.47328475262359, std=31.79330742742731):
    return (data * std + mean)

def predict(vm_id: int, TIME_STEPS:int, threshold:float, tail_len:int):
    vmid = str(vm_id)
    file_path = os.path.join('sql_app/cpu_data', vmid + ".json")
    if os.path.exists(file_path):
        with open(file_path, 'r') as file:
            cpu_used = json.load(file)
        cpu_use_df=convert_json_dataframe(cpu_used)
        data_append=train_scaler(cpu_use_df)
        #TIME_STEPS=30  
        x_test = create_dataset(data_append[["cpu_usage"]],data_append.cpu_usage,TIME_STEPS)
        last_data=x_test[-1]
        last_data = last_data.astype(np.float32)
        last_data_list = last_data.reshape((1, TIME_STEPS, 1)).tolist()
        predictionfromapi = endpoint.predict(instances=last_data_list)
        predictionres = inverse_train_scaler_array(predictionfromapi.predictions[0][0][0])
        file_pathpred = os.path.join('sql_app/cpu_data', vmid + "pred.json")
        response = predictionres

        if os.path.exists(file_pathpred):
            with open(file_pathpred, 'r') as file:
                cpu_usedpred = json.load(file)
            dic_key = next(iter(cpu_usedpred))
            cpu_usedpred[dic_key].append(response)
            print(cpu_usedpred[dic_key])
            with open(file_pathpred, 'w') as file:
                json.dump(cpu_usedpred, file)
            if (len(cpu_usedpred[dic_key])) >= tail_len:
                average_actual_data=cpu_use_df.tail(tail_len).cpu_usage.values.mean()
                average_forecast_data=np.array(cpu_usedpred[dic_key][-3:]).mean()
                if (average_actual_data-average_forecast_data)>threshold:
                    response = 'anomaly' 
                else:
                    response = 'no anomaly'
        else:
            cpu_usedpred = {'cpu_usage': [response]}
            with open(file_pathpred, 'w') as file:
                json.dump(cpu_usedpred, file)
    else:
        response = "No data has been recorded yet"
    return response

def resource(vm_id: int, recap: int, threshold:float):
    vmid = str(vm_id)
    file_path = os.path.join('sql_app/cpu_data', vmid + ".json")
    response = "nothing"
    if os.path.exists(file_path):
        with open(file_path, 'r') as file:
            cpu_used = json.load(file)
        cpu_use_df=convert_json_dataframe(cpu_used)
        iterate= int(recap/5) #menit
        last_1_hour_average=cpu_use_df.tail(iterate).cpu_usage.mean()
        print(last_1_hour_average)
        if threshold-1<=last_1_hour_average:
            response = 'you need additonal resource because your average last hour usage ',last_1_hour_average,'%'
    else:
        response = "No data has been recorded yet"
    return response

   
