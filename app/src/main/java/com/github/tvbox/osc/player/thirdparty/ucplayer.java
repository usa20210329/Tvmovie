​
35
    private static final ucPackageInfo[] PACKAGES = {
36
            new ucPackageInfo(PACKAGE_NAME, PLAYBACK_ACTIVITY),
37
    };
38
​
39
    public static ucPackageInfo getPackageInfo() {
40
        for (ucPackageInfo pkg : PACKAGES) {
41
            try {
42
                ApplicationInfo info = App.getInstance().getPackageManager().getApplicationInfo(pkg.packageName, 0);
43
                if (info.enabled)
44
                    return pkg;
45
                else
46
                    Log.v(TAG, "ucplayer package `" + pkg.packageName + "` is disabled.");
47
            } catch (PackageManager.NameNotFoundException ex) {
48
                Log.v(TAG, "ucplayer package `" + pkg.packageName + "` does not exist.");
49
            }
50
        }
51
        return null;
52
    }
53
​
54
    public static boolean run(Activity activity, String url, String title, String subtitle, HashMap<String, String> headers) {
55
        ucPackageInfo packageInfo = getPackageInfo();
56
        if (packageInfo == null)
57
            return false;
58
​
59
        Intent intent1 = new Intent();
60
        intent1.setAction(Intent.ACTION_VIEW);
61
        intent1.setData(Uri.parse(url));
62
        intent1.putExtra("title", title);
63
        intent1.putExtra("name", title);
64
        inten1t.putExtra("uc.extra.title", title);
65
            if (headers != null && headers.size() > 0) {
66
            try {
67
                JSONObject json = new JSONObject();
68
                for (String key : headers.keySet()) {
69
                    json.put(key, headers.get(key).trim());
70
                }
71
                intent.putExtra("uc.extra.http_header", json.toString());
72
            } catch (JSONException e) {
73
                e.printStackTrace();
74
            }
75
        }
76
        if (subtitle != null && !subtitle.isEmpty()) {
77
            intent1.putExtra("uc.extra.subtitle", subtitle);
78
        }
79
        try {
80
            startActivity(Intent.createChooser(intent1,getString(R.string.about_cherry_choice_browser)));
81
            return true;
82
        } catch (ActivityNotFoundException ex) {
83
            Log.e(TAG, "Can't run ucplayer", ex);
84
            return false;
85
        }
86
    }
87
}
88
