# FAOSDance ![GitHub Last Commit](https://img.shields.io/github/last-commit/DeflatedPickle/FAOSDance.svg) [![Build Status](https://travis-ci.org/DeflatedPickle/FAOSDance.svg?branch=master)](https://travis-ci.org/DeflatedPickle/FAOSDance)
A stand-alone, free and open-source implementation of the Fruity Dance plugin from FL Studio.

| | Downloads |
|---|---|
| **Latest** | ![GitHub Releases (by Release)](https://img.shields.io/github/downloads/DeflatedPickle/FAOSDance/v0.1.17-alpha/total.svg) |
| | ![GitHub Releases (by Release)](https://img.shields.io/github/downloads/DeflatedPickle/FAOSDance/v0.1.13-alpha/total.svg) |
| | ![GitHub Releases (by Release)](https://img.shields.io/github/downloads/DeflatedPickle/FAOSDance/v0.1.5-alpha/total.svg) |
| | ![GitHub Releases (by Release)](https://img.shields.io/github/downloads/DeflatedPickle/FAOSDance/v0.1.2-alpha/total.svg) |
| | ![GitHub Releases (by Release)](https://img.shields.io/github/downloads/DeflatedPickle/FAOSDance/v0.1.0-alpha/total.svg) |

### Comparison With Fruity Dance
| | Fruity Dance | FAOSDance |
|---|---|---|
| **Interface** | ![FruityDance](.github/images/FruityDance.png) | ![FAOSDance](.github/images/FAOSDance.png) |
| **Pros** | Has a frame blending slider | Free, stand-alone and more customizable, has a config file |
| **Cons** | Requires owning (paid or trial) and running FL Studio | *Coming Soon* |

---

### How To Use
#### Setting Up
- Download the JAR from the [latest release](https://github.com/DeflatedPickle/FAOSDance/releases/latest)
- Download the FL-Chan (or similar) sprite sheet and text file (can be found in the FL Studio files, or you can [download a big version](http://www.image-line.com/support/FLHelp/content/FLChan_HD.zip))
- Run the program
    - Click the big "Open" button
        - Locate the FL-Chan (or similar) sprite sheet and text file
            - Double-click either one, or click it and then click "Open"

#### Running On Start-Up
If you want to run the program on start-up, you can using a couple scripts. These will find the latest version of the program in a directory and run it.
It's a good idea to add a config, so it starts up without a prompt window.
- Download the files in the `scripts` folder
    - Place `FAOSDance.ps1` in the same directory as you put `FAOSDance.jar` in
    - Place `FAOSDance.bat` in `C:\ProgramData\Microsoft\Windows\Start Menu\Programs\Startup`
- Open `FAOSDance.bat` and change `./FAOSDance.ps1` to the full path of where you put `FAOSDance.ps1`

### Using The Config
#### Showing File Extensions
- Using the File Explorer, navigate to the `View` menu, click it
    - Navigate to the `Options` button, click it
        - Change to the `View` tab
            - Untick the `Hide extensions for known file types` checkbox
                - Click the `Ok` button
#### Editing Files In The Program
##### Option 1
- Right-click on the program file
    - Select the `Rename` option
        - Change the extension (the text shown after the dot) to `zip`
- Double-click the program file
- Open the `config.toml` file and configure it using the details below

##### Option 2
- Right-click the program file
    - Open it with an archiving program, such as; WinRAR, 7-Zip or WinZip
- Open the `config.toml` file and configure it using the details below

#### Config Options
| Option | Description |
|---|---|
| `sprite.sheet` | `(String)` The path to the sprite sheet name to use, without any extensions |
| `sprite.action` | `(String)` The action to start play through |
| `sprite.fps` | `(Integer)` The amount of delay between each frame |
| `sprite.visible` | `(Boolean)` Whether or not the sprite is visible |
| `sprite.solid` | `(Boolean)` Whether or not the sprite can be clicked and dragged |
| `sprite.always_on_top` | `(Boolean)` Whether or not the program stays above other windows |
| `animation.play` | `(Boolean)` Whether or not the animation is playing |
| `animation.rewind` | `(Boolean)` Whether or not the animation is playing backwards |
| `animation.frame` | `(Integer)` The current frame of animation |
| `location.x` | `(Integer)` The location of the window along the X axis |
| `location.y` | `(Integer)` The location of the window along the Y axis |
| `size.width` | The width of the sprite |
| `size.height` | The height of the sprite |
| `reflection.padding` | The amount of padding (centered) between the sprite and the reflection |
| `reflection.fade.height` | The amount of the reflection hidden, from the bottom up |
| `reflection.fade.opacity` | The opacity of the reflection |
#### Config Example
```toml
[sprite]
sheet="path/to/file/without/extensions"
action="Zitabata"
fps=32

[size]
width=1.0
height=1.0
```