import os
import sys
import glob
import pandas as pd

import matplotlib.pyplot as plt 
import numpy as np 
import csv 

def plot(means, dims, testType, save_path, method):
    x = dims
    # x = np.log(dims)
    plt.errorbar(x, means, yerr=0, fmt='-o', label= method + ' Thread')
    plt.xticks(x,dims)
    plt.legend(loc='best')
    plt.title('Test on ' + testType + ' number')
    plt.ylabel('Speed Up')
    plt.xlabel('Testing values')
    plt.savefig(save_path + '/graph.png')
    #plt.show()

def read_multiple_csv(plotting_data):

    mean = []
    std = []
    axes = []

    files = os.listdir(plotting_data + "/")  
    files.sort(key=lambda x: int(''.join(filter(str.isdigit, x))))  


    for filename in files:
        data_df = pd.read_csv(plotting_data + "/" + filename, error_bad_lines=False, names=["mean", "std", "queries"])
        mean.append(data_df["mean"].values[0])
        std.append(data_df["std"].values[0])
        axes.append(data_df["queries"].values[0])

    return mean, std, axes

def read_csv(plotting_data):

    mean = []
    std = []
    axes = []

    data_df = pd.read_csv(plotting_data, error_bad_lines=False, names=["mean", "std", "queries"])

    for i in range(len(data_df["mean"].values)):
        mean.append(data_df["mean"].values[i])
        std.append(data_df["std"].values[i])
        axes.append(data_df["queries"].values[i])

    return mean, std, axes


if __name__ == "__main__":

    plotting_data = sys.argv[1]
    testType = sys.argv[2]
    save_path = sys.argv[3]
    method = sys.argv[4]

    mean = []
    std = []
    axes = []

    if testType == "pss":
        mean, std, axes = read_multiple_csv(plotting_data)

    else:
        mean, std, axes = read_csv(plotting_data)

    plot(mean, axes, testType, save_path, method)



