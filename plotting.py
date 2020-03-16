import os
import sys
import glob
import pandas as pd

import matplotlib.pyplot as plt 
import numpy as np 
import csv 

#def plot_values(means, stds):
#    plt.plot (dims, means, '-o')
#    plt.fill_between(dims,means-stds,means+stds,alpha=.1)
#    plt.show()

def read_multiple_csv(path):

        mean = []
        std = []
        axes = []

        files = glob.glob(path + "/*.csv")

        
        for filename in files:
            data_df = pd.read_csv(path + '/'+ filename, error_bad_lines=False, names=["mean", "std", "queries"])
            mean.append(data_df["mean"].values[0])
            std.append(data_df["std"].values[0])
            axes.append(data_df["queries"].values[0])

        return mean, std, axes

def read_csv(path):

    mean = []
    std = []
    axes = []

    filename = os.listdir(path + '/')
    data_df = pd.read_csv(path + '/' + filename[0], error_bad_lines=False, names=["mean", "std", "queries"])

    for i in range(data_df["mean"].values):
        mean.append(data_df["mean"].values[i])
        std.append(data_df["std"].values[i])
        axes.append(data_df["queries"].values[i])


if __name__ == "__main__":

    path = sys.argv[1]
    test_method = sys.argv[2]

    mean = []
    std = []
    axes = []

    if test_method == "pss":
        mean, std, axes = read_multiple_csv(path)

    else:
        mean, std, axes = read_csv(path)


    #means = []
    #stds = []
    #dims = []
    #with open('naive.csv') as csvFile:
    #    csv_reader = csv.reader(csvFile, delimiter = ',')
    #    for line in csvFile:
    #        m, s, d = line.replace('\n','').split(',')
    #        means.append(float(m))
    #        stds.append(float(s))
    #        dims.append(int(d))
    # print(means)
    # print(stds)

    #plot_values(np.asarray(means), np.asarray(stds))



