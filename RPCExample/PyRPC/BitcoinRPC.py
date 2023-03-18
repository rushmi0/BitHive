#!/usr/bin/env python3

import json
import requests

rpc_user = 'somename'
rpc_password = 'somepassword'

""" JSON-RPC connections on <port> """
# default: 8332
# testnet: 18332
# signet: 38332
# regtest: 18443
rpc_port = 8332

auth = requests.auth.HTTPBasicAuth(rpc_user, rpc_password)
headers = {'content-type': 'application/json'}
url = f'http://localhost:{rpc_port}/'

method = 'getblockcount'
params = []

payload = {
    'method': method,
    'params': params,
    'jsonrpc': '2.0',
    'id': 0,
}

response = requests.post(url, data=json.dumps(payload), headers=headers, auth=auth)

if response.status_code != 200:
    print(f"HTTP Error: {response.status_code}")
else:
    try:
        result = response.json()['result']
        if result is None:
            print("Result is null")
        else:
            print('Block Height : %s ' % (result))
    except json.decoder.JSONDecodeError as e:
        print(f"JSON Error: {e}")
