# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Changed
- Tapping notification now launches the "Now playing" screen ([#179])

### Fixed
- Fixed an issue where removing a track from a playlist removed it from all playlists ([#69])

## [1.6.0] - 2025-11-09
### Changed
- Player now respects play/pause state when seeking ([#97])

### Fixed
- Fixed startup crash with large number of items
- Fixed another crash when clearing app from recents ([#298])
- Fixed flicker when skipping to the next/previous track on the player screen

## [1.5.1] - 2025-11-05
### Fixed
- Fixed player crash when clearing app from recents ([#298])
- Fixed overlap between current track and system bars

## [1.5.0] - 2025-10-29
### Changed
- Compatibility updates for Android 15 & 16
- Updated translations

## [1.4.0] - 2025-10-14
### Added
- Support for folders on Android 9 and below versions ([#258])

### Changed
- Updated translations

## [1.3.0] - 2025-10-09
### Changed
- Search query is now preserved when switching tabs ([#261])
- Search now ignores accents and diacritics ([#209])
- Updated translations

### Fixed
- Fixed issue with track numbers displaying as zero in some cases ([#269])

## [1.2.3] - 2025-09-09
### Changed
- Updated translations

### Fixed
- Playlist menu options now show up correctly on the playlists tab ([#65])

## [1.2.2] - 2025-08-21
### Changed
- Updated translations

### Fixed
- Disc numbers now display correctly in multi-disc albums ([#228])

## [1.2.1] - 2025-07-16
### Changed
- Updated translations

### Fixed
- Fixed unknown error when updating ID3 tags ([#206])

## [1.2.0] - 2025-07-15
### Changed
- Updated translations

## [1.1.0] - 2025-05-20
### Added
- Support for displaying disc number in albums ([#18])

### Changed
- Turned off shuffle by default
- Replaced checkboxes with switches
- Previous button now supports replay ([#45])

### Removed
- Dropped support for Android 7 and older versions

### Fixed
- Fixed track numbers displaying as 0 ([#47])

## [1.0.0] - 2024-02-11
### Added
- Initial release

[#18]: https://github.com/FossifyOrg/Music-Player/issues/18
[#45]: https://github.com/FossifyOrg/Music-Player/issues/45
[#47]: https://github.com/FossifyOrg/Music-Player/issues/47
[#65]: https://github.com/FossifyOrg/Music-Player/issues/65
[#69]: https://github.com/FossifyOrg/Music-Player/issues/69
[#97]: https://github.com/FossifyOrg/Music-Player/issues/97
[#179]: https://github.com/FossifyOrg/Music-Player/issues/179
[#206]: https://github.com/FossifyOrg/Music-Player/issues/206
[#209]: https://github.com/FossifyOrg/Music-Player/issues/209
[#228]: https://github.com/FossifyOrg/Music-Player/issues/228
[#258]: https://github.com/FossifyOrg/Music-Player/issues/258
[#261]: https://github.com/FossifyOrg/Music-Player/issues/261
[#269]: https://github.com/FossifyOrg/Music-Player/issues/269
[#298]: https://github.com/FossifyOrg/Music-Player/issues/298

[Unreleased]: https://github.com/FossifyOrg/Music-Player/compare/1.6.0...HEAD
[1.6.0]: https://github.com/FossifyOrg/Music-Player/compare/1.5.1...1.6.0
[1.5.1]: https://github.com/FossifyOrg/Music-Player/compare/1.5.0...1.5.1
[1.5.0]: https://github.com/FossifyOrg/Music-Player/compare/1.4.0...1.5.0
[1.4.0]: https://github.com/FossifyOrg/Music-Player/compare/1.3.0...1.4.0
[1.3.0]: https://github.com/FossifyOrg/Music-Player/compare/1.2.3...1.3.0
[1.2.3]: https://github.com/FossifyOrg/Music-Player/compare/1.2.2...1.2.3
[1.2.2]: https://github.com/FossifyOrg/Music-Player/compare/1.2.1...1.2.2
[1.2.1]: https://github.com/FossifyOrg/Music-Player/compare/1.2.0...1.2.1
[1.2.0]: https://github.com/FossifyOrg/Music-Player/compare/1.1.0...1.2.0
[1.1.0]: https://github.com/FossifyOrg/Music-Player/compare/1.0.0...1.1.0
[1.0.0]: https://github.com/FossifyOrg/Music-Player/releases/tag/1.0.0
