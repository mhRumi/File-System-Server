#include<bits/stdc++.h>
using namespace std;
int lps(string &s){
    int n=s.size();
    vector<int> v(n,0);
    for(int i=1,j=0;i<n;i++,j=v[i-1]){
        while(j>0 && s[j]!=s[i])j=v[j-1];
        v[i]=j+(s[i]==s[j]);
    } return v.back();
}

int main(){
    ios_base::sync_with_stdio(0); cin.tie(0); cout.tie(0);
    string s; while(cin>>s && s!="."){
        int l=s.size(),x=lps(s),a=l-x,b=(l%a==0?a:l);
        cout<<l/b<<endl;
    }
}