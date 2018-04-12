# Truck-Factor

This is a tool for estimating the Truck Factor of GitHub projects, using information from commit history. Truck Factor (also known as Bus Factor or Lottery Number) is the minimal number of developers that have to be hit by a truck (or leave) before a project is incapacitated.

## Environment 

The scripts for extract commit information from git repositories are implemented using Shell and AWK. So, the execution environment must support those script languages.  Optionally, the Ruby interpreter is required if you decide to use the Linguist library to automatically discard files like documentation and third-party libraries. See the specific Linguist requirements in [linguist page](https://github.com/github/linguist).

## Usage

Get the last version of the [truckfactor-tool](https://github.com/mtov/Truck-Factor/releases)

To run the tool to perform these steps:

1. Clone the repository to be analysed.
	- example: ```git clone https://github.com/mtov/Truck-Factor.git```
2. Use the scripts in the folder `scripts` to extract information from the git repository to be analyzed:
    1. Extract commit and file information. 
        - command: ```./commit_log_script.sh  <git_repository_path>```
        - example: ```./commit_log_script.sh  git/truck-factor```
	
	
    2. Extract files to be discard using Linguist library (Optional)
        - command: ```./linguist_script.sh <git_repository_path>```
        - example: ```./linguist_script.sh git/truck-factor```
	
3. Execute the gittruckfactor tool.
    - command: ```java –jar gittruckfactor.jar <git_repository_path> <git_repository_fullname>```
    - example: ```java –jar gittruckfactor.jar git/truck-factor aserg-ufmg/Truck-Factor```

## Optional Settings

Repository specifc information can be provided using the files in the folder `repo_info`, which  can improve the TF calculation results. The additional information supported are:

* Filtered files (`filtered-files.txt`): set files that must be discard before start the TF calculation. 
  * Info pattern: `<git_repository_fullname>;<file_path>;<filter_info>`
* Aliases (`alias.txt`): set developers aliases.
  * Info pattern: `<git_repository_fullname>;<developer_alias1>;<developer_alias2>`
* Modules (`modules.txt`): map files to modules. 
  * Info pattern: `<git_repository_fullname>;<file_path>;<module_name>`
  * * Module calculation not implemented yet.

### Run-time settings
Algorithm's variables can be set by modifying the `config.properties `file.

## Example

Here are the TF values as estimated by this tool for some popular GitHub projects (using data from November, 2016):

* FreeCodeCamp: 1
* Bootstrap: 3
* D3: 1
* React: 4
* AngularJS: 6
* Font-Awesome: 1
* jquery: 4
* electron: 1
* tensorflow: 2
* docker: 13
* meteor: 5
* swift: 5
* vue: 1
* rails: 11
* atom: 4

## More Info

Guilherme Avelino, Leonardo Passos, Andre Hora, Marco Tulio Valente. [A Novel Approach for Estimating Truck Factors](https://arxiv.org/abs/1604.06766). In 24th International Conference on Program Comprehension (ICPC), pages 1-10, 2016.
