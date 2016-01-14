# restore trained data
import tensorflow as tf
import numpy as np
import json
import sys
sys.path.append('mnist')
import model

x = tf.placeholder("float", [None, 784])
sess = tf.Session()

with tf.variable_scope("simple"):
    y1, variables = model.simple(x)
saver = tf.train.Saver(variables)
saver.restore(sess, "mnist/data/simple.ckpt")
def simple(input):
    return sess.run(y1, feed_dict={x: input}).flatten().tolist()

with tf.variable_scope("convolutional"):
    keep_prob = tf.placeholder("float")
    y2, variables = model.convolutional(x, keep_prob)
saver = tf.train.Saver(variables)
saver.restore(sess, "mnist/data/convolutional.ckpt")
def convolutional(input):
    return sess.run(y2, feed_dict={x: input, keep_prob: 1.0}).flatten().tolist()

# webapp
from flask import Flask, jsonify, render_template, request

app = Flask(__name__)

@app.route('/image_send', methods=['POST'])
def mnist():
    array = json.loads(request.data)["data"]
    for i in range(784):
        if (array[i] > 200):
            array[i] =255
        elif (array[i] <150):
            array[i] = 0

    for i in range(28):
        new_arr = []
        for j in range(28):
            new_arr.append(array[28*i+j])
        print new_arr
    input = ((np.array(array, dtype=np.uint8)) / 255.0).reshape(1, 784)

    #output1 = simple(input)
    output2 = convolutional(input)
    max_output = output2.index(max(output2))
    print max_output
    return str(max_output)

if __name__ == "__main__":
    app.run(host='0.0.0.0', port ='20000')
