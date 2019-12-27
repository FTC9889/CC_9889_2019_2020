import matplotlib.pyplot as plt
import argparse
import sys

def main():
    arg_parser = argparse.ArgumentParser(description='Plot CSV')
    arg_parser.add_argument('file', metavar='FILE', type=str, \
                            help='The file from which to plot.')

    args = arg_parser.parse_args(sys.argv[1:])

    plots = []
    xy = False

    with open(args.file+".csv", 'r') as fd:
        for line in fd:
            newLine = line.split(",")

            try:
                for i in range(len(newLine)):
                    if newLine[i] != 'x':
                        newLine[i] = eval(newLine[i])

                        if len(plots) != len(newLine):
                            plots.append([])

                        plots[i].append(newLine[i])
                    else:
                        xy = True

            except:
                print("Plotting as an (X, Y)")

    if not xy:
        defaultGraph = "or"
        listOfPointTypes = [
            "-or", "-ob", "oy"
        ]

        listOfLabels = [
            "Pos", "Vel", "Acc", "Jerk"
        ]
        plt.figure(args.file)
        for i in range(len(plots)):
            if i < len(listOfPointTypes):
                pointType = listOfPointTypes[i]
            else:
                pointType = defaultGraph

            if i< len(listOfLabels):
                ArrayLabel = listOfLabels[i]
            else:
                ArrayLabel = ""

            plt.plot(range(0, len(plots[i])), plots[i], pointType,label = ArrayLabel)
        plt.xlabel("Time")
        plt.legend()
        plt.show()
    else:
        plt.plot(plots[0], plots[1])
        plt.show()

main()