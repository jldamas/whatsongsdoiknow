#!/usr/bin/perl

use LWP::Simple;

my @chordPages = ();
my $pageRoot = "http://www.ultimate-guitar.com";

my $maxSearchPages = 377;
my $pageNum = 1;

my $searchResultsPage;

my $reRead = $ARGV[0];

if ($reRead) {
    while ($pageNum < $maxSearchPages) {
	if ($pageNum == 1) {
	    $searchResultsPage = "http://www.ultimate-guitar.com/search.php?bn=&sn=&type[]=3&type2[]=1&level[]=novice&level[]=intermediate&tuning[]=";
	} else {
	    $searchResultsPage = "http://www.ultimate-guitar.com/search.php?bn=&sn=&type[]=3&type2[]=1&level[]=novice&level[]=intermediate&tuning[]=&npage=$pageNum";
	}

	my $page = get($searchResultsPage);
	my @lines = split(/\n/, $page);
	my $chordUrl;
	
	foreach $line (@lines) {
	    if ($line =~ /<a href=\"(\S+tabs\/(\S+)_crd\.htm)/) {
		$chordUrl = $1;
		push(@chordPages, $pageRoot . $chordUrl);
		print $pageRoot . $chordUrl . "\n";
	    }
	}
	
	$pageNum++;
    }
} else {
    while (<>) {
	chomp;
	push(@chordPages, $_);
    }
}

my $id = 1;

foreach $chordPage (@chordPages) {
    my $name;
    my $artist;

    $chordPage =~ /\/tabs(\S+)\/(\S+)\/(\S+)_crd.htm/;

    $artist = $2;
    $name = $3;

    $name =~ s/_/ /g;
    $artist =~ s/_/ /g;

    print "<S ID=$id NAME=\"$name\" ARTIST=\"$artist\">\n";

    my $content = get($chordPage);
    $content =~ /\<pre\>((\S|\s)+)\<\/pre\>/;

    my $pre = $1;
    my @preLines = split(/\n/, $pre);
    my $re = '\<a id=\'ch(.+)\>(\S+)\<\/a\>';

    foreach $pl (@preLines) {
	if ($pl =~ /$re/) {
	    my @lineChord = split(/\<\/a\>/, $pl);

	    foreach $lc (@lineChord) {
		if ($lc =~ /(\s*)\<a id=\'ch(.+)\>(\S+)(\<\/a\>)*/) {
		    #print $lc."\n";
		    print "$1$3";
		}
	    }
	    print "\n";
	} else {
	    print $pl . "\n";
	}
    }

    print "</S>\n";

    $id++;
}
