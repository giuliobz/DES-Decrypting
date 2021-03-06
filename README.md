# DES-Decrypting

This repository is a collaboration with [Niccolò Biondi](https://github.com/NiccoBiondi) as the mid-term project for the [Parallel Computing](https://www.unifi.it/p-ins2-2015-415640-0.html) exam at the University of Florence. The full-term project, Pattern Recognition, can be found in [this repository](https://github.com/NiccoBiondi/Pattern_Recognition).

Here we propose our solutions for the des-Decrypting task. In particular, we study the CPU parallelism, through Java Thread implementations. We compare those performances with a sequential method and we evaluate the resulting Speed Up and Efficiency.

In [DictResults](DictResults) there are all the experimental results.

For more theorical details of our project please refers to the review and our presentation in the [docs](docs) subdirectory.
## Reproduce experiments

### Install  the repository

* `$ git clone https://github.com/giuliobz/DES-Decrypting.git`
* `$ cd DES-Decrypting-master`

### Create the Database

To crete the dictionary use the file [PasswordBuilder](src/PasswordDictionary/PasswordBuilder.java) and specify three arguments:

- the starting dictionary to modify
- file name to save the password dictionary
- method used: **data** to ceate a Dictionary of birth date or **words** to create a Dictionary of words


## Install javafx

To use javafx follow te instruction [there](https://www.jetbrains.com/help/idea/javafx.html)

## Build Artifact

After run the scritpt, package your application in a JAR using [this](https://www.jetbrains.com/help/idea/packaging-a-module-into-a-jar-file.html) 

### Run the experiments

Inside the DES-Decrypting folder there is a script.sh that the user can use to reproduce the experiments made:


* `$ cd DES-Decrypting-master`
* `$ chmode a+x script.sh`
* `$ ./script.sh`

NB: changing the packet JAR path