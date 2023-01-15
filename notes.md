# How to convert individual frames to video

Example:
```bash
ffmpeg -framerate 30 -pattern_type glob -i './video/*.png' -i ./data/HS.mp3 -c:v libx264 -r 30 -pix_fmt yuv420p out.mp4
```