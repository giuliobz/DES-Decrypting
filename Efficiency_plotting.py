import os
import sys
import glob
import pandas as pd

import matplotlib.pyplot as plt
import numpy as np 
import csv 


def multy_plot(efficiency, dims, testType, save_path):
    x = dims

    
    plt.errorbar(x, efficiency[0], yerr=0, fmt='-o', label='Callable')
    plt.errorbar(x, efficiency[1], yerr=0, fmt='-o', label='Runnable')
    plt.errorbar(x, efficiency[2], yerr=0, fmt='-o', label='Lock')
    plt.errorbar(x, efficiency[3], yerr=0, fmt='-o', label='Sync')
    plt.xticks(x,dims)
    plt.legend(loc='best')
    plt.title('Test on ' + testType + ' number')
    plt.ylabel('Speedup')
    plt.xlabel('Testing values')
    plt.savefig(save_path + '/Dictionary_data.png')
    plt.show()

def read_multiple_csv(plotting_data):

    mean = []
    std = []
    axes = []

    files = os.listdir(plotting_data + "/")
    files = [f for f in files if f != 'graph.png']
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

    path = "/home/bazza/Scrivania/DES-Decrypting/DictResults/"
    plotting_data = ["Callable_Dictionary_data_thread/CallableSpeedup_100.csv", "Runnable_Dictionary_data_thread/RunnableSpeedup_100.csv",
                     "Lock_Dictionary_data_thread/LockSpeedup_100.csv", "Sync_Dictionary_data_thread/SyncSpeedup_100.csv"]
    
    testType = "thread"
    save_path = os.path.dirname(os.path.realpath(__file__))

    efficiency = []
    std = []
    axes = []

    for k, plty in enumerate(plotting_data):
        if "thread" in testType:
            tmp_mean, tmp_std, tmp_axes = read_csv(path + plty)
        else:
            tmp_mean, tmp_std, tmp_axes = read_multiple_csv(path + plty)

        efficiency.append([])
        
        axes.append(tmp_axes)

        for i, m in enumerate(tmp_mean):
            efficiency[k].append(m/axes[i])

    multy_plot(efficiency, axes[0], testType, save_path)
