# John Conway's Game Of Life 🐣
This was a project for my CT255 Next Generation Technologies I: 2D Games Programming with indie game dev Sam Redfern.<br>

## Overview
Conway was a mathematician, who in 1970, made one of the first examples of cellular automata, "The Game of Life", or simply "Life".<br>
Regarded as a "zero-player" game, the game's evolution is determined by the initial state set out by the player.<br>
Each 20x20 pixel, or "cell", is either dead or alive. Each alive cell is represented as white, and each dead by black.<br>
<br>
The evolution of these cells is determined by a ruleset, at each "generation" you're either born, killed, remain alive, or remain dead.<br>
The ruleset is overseen by the states of a cell's 8 neighbours in a 3x3 grid (with the current cell being in the centre).<br>
<br>
This code also features a loading and saving feature, through converting the boolean value of each cell to a binary digit, and then saving all states as a String in a .txt file.<br>
Note that it only allows 1 save at a time and upon pressing the save button again, the previous save will be overriden.<br>

### Ruleset
" **For a space that is populated:** <br>
- Each cell with one or no neighbors dies, as if by solitude.<br>
- Each cell with four or more neighbors dies, as if by overpopulation.<br>
- Each cell with two or three neighbors survives.<br>
<br>
 **For a space that is empty or unpopulated:** <br>
- Each cell with three neighbors becomes populated. "<br><br>
**Credit:** https://playgameoflife.com/info<br>

## Instructions
Code is contained within one .java file, to make use of loading and saving, you must change the directory for the .txt save file manually.<br>
Make sure you have Java installed on your system. This project requires the following Java modules:<br>
- **java.base**: The foundational module providing core Java APIs.
- **java.desktop**: The module for desktop-related functionality and GUI operations.
