import requests
import os
import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from email.mime.base import MIMEBase
from email import encoders
import time
from selenium import webdriver
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
import ctypes


'''#####################################################################################################################
# Date of Submission -                                                                                                 #
# By - Rudra Madhav Biswal                                                                                             #
# 1601106117 , IT - 6th SEM                                                                                            #
# CNDC Assignment                                                                                                      # 
#####################################################################################################################'''


url = 'https://api.myjson.com/bins/11tt7a'
headers = {'content-type': 'application/json'}
temp="nothing"


while True:

    req = requests.get(url)
    data_received = req.json()

    if "sup_a" in data_received:
        data_received_string = data_received["sup_a"]
        print(data_received_string)
        if len(data_received_string) != 0:
            print ("Command Received")

        if "shutdown" in data_received_string:
            payload = {"sup_p": "Shutting Down PC"}
            response = requests.put(url, json=payload)
            print(response.status_code)
            os.system('shutdown /p /f')
            exit()


        elif "lock" in data_received_string:
            payload = {"sup_p":"PC Has Been Locked"}
            response = requests.put(url,json=payload)
            print(response.status_code)
            ctypes.windll.user32.LockWorkStation()

        elif "send me file" in data_received_string:
            payload = {"sup_p": "I am on it"}
            response = requests.put(url, json=payload)
            print(response.status_code)
            print(data_received_string)
            filename=data_received_string[13:]
            print(filename)
            print("Attaching File...")
            sender_email = "talkPC2019@gmail.com"
            receiver_email = "rickrudra@gmail.com"
            msg = MIMEMultipart()
            msg['From'] = sender_email
            msg['To'] = receiver_email
            msg['Subject'] = "Here's the file you requested"
            body = "See Attachment"
            msg.attach(MIMEText(body, 'plain'))
            attachment = open(filename, "rb")
            p = MIMEBase('application', 'octet-stream')
            p.set_payload(attachment.read())
            encoders.encode_base64(p)
            p.add_header('Content-Disposition', 'attachment;filename='+filename)
            msg.attach(p)
            s = smtplib.SMTP('smtp.gmail.com', 587)
            s.starttls()
            s.login(sender_email, "emophheg@24")
            text = msg.as_string()
            print("Sending Mail...")
            s.sendmail(sender_email, receiver_email, text)
            s.quit()
            print("Mail Has Been Sent")

        elif "play" in data_received_string:
            payload = {"sup_p":"Now Playing"}
            response=requests.put(url,json=payload)
            print(response.status_code)
            find_item = data_received_string[5:]
            driver = webdriver.Firefox()
            driver.get(find_item)
            wait = WebDriverWait(driver,5);
            time.sleep(10)
            find = wait.until(EC.visibility_of_element_located((By.CLASS_NAME, "html5-main-video")))
            find.click()
            time.sleep(2)
            find = wait.until(EC.visibility_of_element_located((By.CLASS_NAME,"ytp-fullscreen-button")))
            find.click()
            time.sleep(2)
            find = wait.until(EC.visibility_of_element_located((By.CLASS_NAME,"html5-main-video")))
            find.click()

    time.sleep(1)
