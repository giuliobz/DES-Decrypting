import os
import sys
import glob
import pandas as pd

import matplotlib.pyplot as plt 
import numpy as np 
import csv 

def multy_plot(means, means1, means2, dims):
    # print(len(means[0]))
    # print(type(dims[0]))
    # yerr plot the stds, 0 means no stds
    x = dims
    # x = np.log(dims)
    # print(x)
    plt.errorbar(x, means, yerr=0, fmt='-o', label='Privatization')
    # plt.errorbar(x, means1, yerr=0, fmt='-o',label='Lock')
    plt.errorbar(x, means2, yerr=0, fmt='-o', label='Query')
    plt.xticks(x,dims)
    plt.legend(loc='best')
    plt.title('Test on threads number')
    plt.ylabel('Speed Up')
    plt.xlabel('Testing values')
    plt.savefig(save_path + '/graph.png')
    plt.show()

def plot(means, dims, testType, save_path, method):
    x = dims
    # x = np.log(dims)
    plt.errorbar(x, means, yerr=0, fmt='-o', label= method + ' Thread')
    plt.xticks(x,dims)
    plt.legend(loc='best')
    plt.title('Test on' + testType + ' number')
    plt.ylabel('Speed Up')
    plt.xlabel('Testing values')
    plt.savefig(save_path + '/graph.png')
    plt.show()

def read_multiple_csv(plotting_data):

        mean = []
        std = []
        axes = []

        files = glob.glob(plotting_data + "/*.csv")

        
        for filename in files:
            data_df = pd.read_csv(plotting_data + '/'+ filename, error_bad_lines=False, names=["mean", "std", "queries"])
            mean.append(data_df["mean"].values[0])
            std.append(data_df["std"].values[0])
            axes.append(data_df["queries"].values[0])

        return mean, std, axes

def read_csv(plotting_data):

    mean = []
    std = []
    axes = []

    data_df = pd.read_csv(plotting_data, error_bad_lines=False, names=["mean", "std", "queries"])

    for i in range(data_df["mean"].values):
        mean.append(data_df["mean"].values[i])
        std.append(data_df["std"].values[i])
        axes.append(data_df["queries"].values[i])


if __name__ == "__main__":

    plotting_data = sys.argv[1]
    testType = sys.argv[2]
    save_path = sys.argv[3]
    method = sys.argv[4]

    print(plotting_data)
    print(testType)
    print(save_path)
    print(method)

    mean = []
    std = []
    axes = []

    if testType == "pss":
        mean, std, axes = read_multiple_csv(plotting_data)

    else:
        mean, std, axes = read_csv(plotting_data)

    plot(mean, axes, testType, save_path, method)



