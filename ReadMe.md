# MidiPlayer-Compose

> 使用 Compose UI 编写的 .mid 文件播放器，可输出到MIDI设备。

## Features

- [x] 拖拽文件播放
- [x] 本机模拟合成播放
- [x] 多设备同时播放
- [ ] 合成sf2选择
- [ ] 播放历史管理
- [ ] 进度控制

## Build

使用IDEA 打开项目，可看到gradle package任务
- JMidiPlayer [packageDmg]
- JMidiPlayer [packageMsi]
- JMidiPlayer [packageUberJarForCurrentOS]


## Bugs

接入新设备需要重启程序，目前定位为Java Midi获取设备问题。
