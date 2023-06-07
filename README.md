# BANGKIT_CLOUDRAYA

Make sure you have python 3.9 to run this project.
We are using FASTAPI as our framework for this project. To install all the dependencies, first install virtualenv

pip install virtualenv

Then you could initiate a virtual environment and activate the environment
virtualenv env
source env/bin/activate

Install all the dependencies
pip install -r requirements.txt

For local development you can start the server by using
uvicorn sql_app.main:app --reload

For deployment on Google Cloud Platform, make sure you have a database, on database.py, please configure your database credential, then run

gcloud app deploy app.yaml

We made a documentation and information for our API requests on https://harirayacloud.as.r.appspot.com/docs
