from simplesauce.options import SauceOptions


class TestSafari(object):

    def test_defaults(self):
        sauce = SauceOptions('safari')

        assert sauce.browser_name == 'safari'
        assert sauce.browser_version == 'latest'
        assert sauce.platform_name == 'macOS 10.14'
