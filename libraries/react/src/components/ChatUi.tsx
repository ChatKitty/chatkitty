import {useEffect} from "react";
import {connectApi, loadChatUi} from "chatkitty";
import './ChatUi.css';

export const ChatUi = () => {
    useEffect(() => {
        const initializeChat = async () => {
            const connection = await connectApi({
                apiKey: "afaac908-1db3-4b5c-a7ae-c040b9684403",
                username: "2989c53a-d0c5-4222-af8d-fbf7b0c74ec6"
            });

            const { user, unreadChannelsCount, notifications, updateUser } = connection;
            console.log("Connected as user:", user.value);

            user.watch((user) => console.log("User:", user));
            unreadChannelsCount.watch((count) => console.log("Unread channels count:", count));
            notifications.watch((notification) => console.log("Received notification:", notification));

            loadChatUi(
                {
                    widgetId: "UWiEkKvdAaUJ1xut",
                    container: { height: "100%" },
                    audio: { enabled: true },
                    components: {
                        chat: (context) => ({
                            menuActions: [],
                            onMounted: () => {
                                console.log("Chat UI mounted with context:", context);
                                updateUser({ properties: { lastUpdated: new Date().toISOString() } });
                            },
                            onHeaderSelected: (channel) => console.log(channel),
                            onMenuActionSelected: (action) => console.log(action)
                        })
                    },
                    templates: {
                    }
                },
                {
                    timeout: 50000,
                    connection
                }
            );
        };

        initializeChat();
    }, []);

    return (
            <div style={{display: "flex", flexDirection: "column", height: "100%", width: "100%"}}>
                <div className="logo-container">
                    <img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAH0AAAB9CAYAAACPgGwlAAAS1ElEQVR4Xu2dd3AUV57Hnb11tf9d1cXd2z8uVN3WXe3aPht713iN16yvFttnu2xjMBkcMCBARIHJOYgkBMbGINskESWRkTASIkhCEUkoByQhUM5Z4nu/32tmNPNmRjMjdU9Sf6q+Zazp6en3vv1e/17sxx5zAwB+SfoP0mukKaRtpLOk26QHpGZSD7ybblITqZyUSgonBZImkF4l/Rvpb+S88RkocY+TniH9mbQXveZ2YnDSjt6bIZg0lPQ06XE577wOSsSzpCGkAFKmIcU6VskhLSa9QHpazkuPhy76SdIw0jVSg0nCdOxTR4qFUvqflPPW44BSRb1HOm+aCp1+E0kaAU80ny7qCdKvSWegPK901KOVdJr0T6Qn5Lx3C3QhvyAtJNWaXqmO6lST5pCekj1wKXQBvyFdhfc3r7wFbgZGkX4le6E5UJpgH5LyzS5Jx1VwpP9X2RfNgBKZTyS1mF+HjotpJI2G1kEelOh8GQZvp4qnwUHzfGj1nIdi+BrSQ7Of1XE3/JyfB7WN5xOSlkM33FNh4+dCrSYdlKBtMqnL7Gd0PI020seyf/2CTvQB9KDNW6gn/UX20CmgtMMLzM+r4+Gkkf5B9tIhoIyQxUgn1PEOuDvcuaYclL50HubTe9q8E46/voQz4/NQBk/0vnTvppT0t7K3VoHSHj8nnUDHO9kPR0o7HfQ+NB4efdjzEA8f6k1+zgPOCw3hOXrDZI/NgNKvzqM4mtDW3IHC2+VI/TkfadEFKMmqQGfb4Gv+s9kPimqRca0IKVF5yLlVisZqzVrFB9FXUEcfDpe/oRaNNS24tDcRpzZdx8mN14RObb5Of0tCZUkderp9P2ZkszkfYkPTERbYmw+siG03qRBUatHnyRn7W9lrAZQm2k3pC6rQ3dmDqP3JOLmhN5Gy4k9noe4B10a+SXtLJzJii3B6+02b+XAmKA5NdTxRRnUiYO3ZTn98GRpNYuQ7+OQmy0TKCt96A1lxJSKDfIXurh6U5Vbh3K4Ei/TKOkE3Q3p0oXwKNaAqBP8pG87960ulA1Uj80axRQJt6cT6WER+n4S7mTwt3rupKqnH9RMZFmnsS9EHuUNNdbiK/0w2nRci5EoHqsbtq4UWiXNE/Oyrq2jSOsJVndamDtymEis/tx0RPwY1Ihqmo3BQVp5oRn9N52cfPwOTI/Ooyu+QT+txPKTyVJhajovfJYqq2iI9DkhD07nk/Lup6bzUSDNsmR4WeAOXf0wRUbz8mayzQfEouVOBrg4eOvYseqgmqi6rx5UDqRbXbSG6GS5+e0sEbRafbdTUdGa2wXBeTMhryzTDmulcElIi89HZ3oUKarJx6ZCPkcU3R/ShNFSV1sNT+nZam9qRcCYLERSEytcrK3zLDWTHl6CNqv97udXUfLU8RmPTowym8+pRTaMmq6ZTwJabUGY8pqO1C+l0nGjSWMkwU3FbP4Wq/OZ6njfQNxwPtDd3ivZxzf1GVNINU1FSi/vFNbhfVIOKu7X0tzrUlDegoaoFrY3tIuK2R0dbFwqoKj+93XqJNRPd4NeOp1Nt0Ns4qqffslbDaWw6z17+ezadlwtr2kZyxHSGOy/qK5tx5adUq6VA1rld8eIcPSYmscnc81dypxJJF3Pp8ZGMC1Sdng2OFx0gHFyZdRDRv8Mo87m5yFXu+T0JiNyfhLiILBQkl6OxtlVU34ZuYz7//YIacYzpeWyJz8mPJe6rMKW+0i2m8zq537HpvD5cUxw13UBP90MqRfdwbneC/YCIPuebpCyvCvkp9xB7NB2nHp3fVieIozqxIRYn1sXSMzhR1EL38qvFjSQfZ018IyVdykVTrfXOFjeZzgHRG2w6bwigKc6aznDB4udlPJU4Dvjk78uyloFqy5Hf4Brqwp5bom+dawhbuMl0ZgqbzjtAaEp/TDelnEpYVEiyUnqtZLSniAO1zOvFIj6xhxtN38amaxq5MwM1nemiZ2JqVL5Dz3p3iJth9VXNDg8Zu9H0MDZd08idGajpHDzdy6tGzOE0jzWdA0VujnFU7whuNP0Gm67ZQK6BgZje0dYpgidrGeRp4huSh4/rKhrtlng3mp7OpmvexdVf06vK6kXTyG4E72HipmFuYpmnBnJ5bLrmOGs6l5LijAf9GrDwJCVfzLPZbexG0ws90vTsuLsI22K/meYNunEyU3Q1y+imP4JLeNbNuw71dHmN6NEUe+S2RYCnm/6I7LgSr6/SbSkuPMtsHqBuOlGaXemzhrM4vWk/F4guZmbQm86jW6d3ODBa5eXim7r40TSwQW06P+suhyRbfO6r4qHYhuoWcaMPStNz4ktx58Zdh2bK+pKiD6WKsfXBZ/oGJbjhQQr5M18Xm51yKc9ql7JPmy4S70tNMyfF4+3y31g+b7ouS+mmD0Lppg9C6aYPQummD0Lppg9C6aZrJO4Vi9yXjCjSuWD7S4hdKd10FcSdQNyvHxdxR0zMaGmw3E6Ht0Apz68R89R5+jLPd5fP4yrppg9Um66JlSq8pMkwutUXPK2NV8fwmL67egp10weg0ztuIj/5nnwpDsOLJCP3JVmcV2vppvdTXEorSwa+spX3f3G18brp/RAbXpZTJV9Cv+EhUEf2i1FLuunOioK2pAu5Ay7hMmW5rpvVo5vupM7ujNdkZyper86bIci/p4V0050ULyvSYPM9QUVxnUsWUOqmOyFeVcI7TWhFd3eP2N1S/l21pZvuhHi3CS33meVFlClR+Ra/q7Z0051Q9GFNNt0zgzczln9XbemmOyqK2hPO8JsoLeFVMw11TaiqqHFItdV16OnpkU8jKKUo3tpkRjXlM6anx2prumF7MmtUPajG4ikbMe7VORj3RzuiY6a+/TXiopPk0wh4Rwytm26Xf0iRf1ZtXGM692XLiVNTPECSee2u/LOCnMx8jB4yE5/+YZbD+mHXUfk0goriWm0XVvK6t9B0+WfVxjWm8+iWlnPbuSnFW4hZo6ayDnPHrbEw1pZGvzILGUnWHxW8T22EA/vc9VdcY6VGFcg/qzauMb2hqllT07mE8KZ/toiLTqbSbmmwhcjw0L28Nbp1eK35ud3xlr+vkvjm5UeIxrjGdO4w4UkLciLVEm+X3debIfizkO3HLE2WtPiLjaiv6XvL+8TzuZrtjME9iryNmsa4yHQ8quI1yCxeMMGbBtrriauurMWcT1dZGG0U1QS3E7Lkr1nAO1pqFcHHnzFfzqwRrjO9raVD7B8jJ3Sg4uqWJz44QvyVFIx+2XpQd+KHszabaqbwZIyEs9kW1zFQ8YoX071jNcR1pjP8Wgs5sQPRqY3XnRpG5Tb7j8HHLQxfOGmd3WrdFI5RbG3d3S9RDcjvd3ER6pre1tKOmPPxuHg8Bomxt1GYU4KGuka0tbahra0d7a3tSP05D8fXX1X2XR3AXDSu1tOvFvW5g5M1Ku5VY+bI5UbDR79sO1rvC55TF7alf9U8p/v4hqsiH46ti0HUj4lorGsW+VRf24D8rGIkRKfhXOgVXA6/jtZm+7tdO4F6potgaccxfPKin9DHz8/AR7+bjg//axrGDPHHVyOWYMHY9VgzcycCPt2CJWN3YPXkPdg4dT+2zjiAoNmh+GbeCXy/6DR+WnYBoWuuGM2VM41Lxu2YQoudlR3lu42HjdX80s+3OFStyxh2wJKf74brPULXz+ng9HC6gmYfwRZKJ6d39eRvsGTMdsz/aBNmvrsac0etxVTKn09fnC3yi/NtJOWfIS+5RdGfa7SBeqZ3d3VjnX+wRdXpiMb8cTbGvuovNG7oHIx/bS4mvD4Pk4YtgN87q7A3IMKYqTyzNfdW6YACnv1bj2HUI9NXTd+B7m7r237Z5aEyl+7iXuUFBcH+RzF9xApx3Xz9nA5Oj0gbpZHTKafdEe1cGSJG+VRCPdN5tkrenSKsmhGEz4YHYORzVNp/T3fsC8odO2qInyhdo1+xHkj1Jc7Io+uuiI39ax14d1t9bSNKC+/bLB2Oml79oFY8DuzBL+pJi83D9Hd6HxuOivOD82XUS0qp5hLOJX3UCzPxxVuLsdoviKr7IrutEydQz3QDnNFcCruo5FdX1CEvsxi3YlIReYqeY/vOYt+2UOxY/T3WB+zE8hmBIoiaNXIFpr77NSa/uUCUCnFzmGjFtK2or7a/9SbDPXD+o1ZhwhtzcSnsqvyxwBHT0xOzMenNeZj81nwkXrM/gtfS1IYNC3ZZXDunh9P1FaVv9qiVCJi8HitmbsGGRcHYuWYfQrYfxcmQ84gKv4ak2HQUZN1FXXWDuCaRlzZu3AGgvumOwonhAKW+phFV92tQXvIAJYVlKMy7i5w7+YiLScK5o5dxlnT/nvUuVmvcuJwoahc29Mu/8ivgLfl+8xHjM335l9usmr4n8CdjadwfFCpuYntUlFeJa75w4goSYlOQl1Ug0nOX0lVeUoGqBzVooFqIA143vnbMfaZrRUZSttF0VmnRfbPPuzq7sGxaoPHzyW8sQGN9s9kxTQ0tWDa995jQkHAtSpy78D3Ta6rqMGX4QqNh6+fvMrbB+bETcegSxr8+p/eZSiU+aNV+o/GdHZ04vCdMVMv8+dihs3EtKsH0J7wd3zO9k0ryvm1HjKayJg6bhzV+O+H3/gqbPXKfvRWAtTOD6dm7RAy8iBuC/us/epUo+T6E75nO1FHJnvXxSnNjHxlp0KQ355PRvTWCtWO4xijIsT5O78UI0+1HKB4ER8md7fbntldX1CJ4zQ8Y/eJMfELNIY7Wudk48rkZ+PrzzchIzqbmWBWC19IxL81SmpWPjvnkBT+s9NuGOyl9L5zg+KC50frbmDwYsd+7R9ddHEB1kMkZiTn4bvMhLPpiA9bOCaJmTb18qAX8vXvUKgj/6RK+XXcIh4LDyOwcunF6jeJj7pdV0DGR+GbtQRzeHYGc2/lobem767O5sQWbAnYj4LP12L3uR6TF36FzdXhDwJfBppuHtx4Cl8LUhAwc2H0S095ZKjotDB07XHKPfGN7soMruHLmprGVwNfF18ddzQd2nUTarUzRDPVQxDtcNH9bk10eKlVlS1MLYs7FY93cnZj2wRKMe603yjYVd2feumq/w0RL8jKLjBG+rPF/moPpHy5B4KI9SIhJEbUGd1N7CBFsuubvZbMFd1AU55XhauRNbFu6V8xI5dLDUbNpJnJJ4gGcz4cHYG/gYVGSVOyL7hfc/MtMzcGxkDOY/dEqpetUahkYrnvKmwH4dtNBxMUkoqxY85dj2WMXm675GxhN4Z6tygfViKDn7IKJazHlfxdgzKvWByIm/nke/MesxI4V+5FyM1M8Mz2oxAj4Gc5t+8LsEuzfehRzx6/G5OHzLdLCGjvUH5+PWIilUwNx6dRVNDY0We0N1Bh/Nn2y/FctObQnjCJqJVqWM4VLysfPTUfAhI04eywK2en5YqGCN8HRfO6dQkSejhFdvCOfVwabzNJKNdkn/zMDk4bNR/jBi/IptGYEm/4aNH6rsoG21naLtjDry7cXYbnfFhzde0ZUf1yaHRlc8WT4+rkUV5ZXI/zAJaz2346v3vsaY/4w2ywPOD7hFoSL4K7J59l0zd+fboAzInDxd3SX+2HMK/7YvOBbMbBSWlSO9jbH5rl5K50dXaL5mHQzDUHLQzB+6FxR2tfP3eX07J8BUEj6Rzb9l3BhBM/mcpubx7y9vTT3F0439xVkJOWivdWlN/uVxwzQ/+yVP9XxSRaamv6G/KmOT/JbU9OfIeXKR+j4FDdIT5ia/jhpiXSQju/AwdNXRsNNjB8CJaTX8T14dud/y56z6c9CqQJ0fI8LMK3aTaEPXpeP1vF6uGr/vey1EfrwSVKk9CUd7+YY6UnZazPogPdImi+S1nEJPEFmuOyxBXTQ03DjcKuOqhyCrWe5DB34K1KtdAId74JnRP2d7K1N6OAnSIvgZZMmdYywb36kx2Vv+wRKEy7G/Fw6XgI30Z6SPXUI+uJvSNZ35dPxVDJJ/yx76RR0gvfh4dOkdYzwNthvyx46DZR++Ykk7bZY1lEDHpgfI/vXb+hkT5GWQc2l8TpqwlODeV12350wzgKl/b4auvGeBhvOI6RPy56pAp8YSol32Uw+nT5hH7Qz3ACU/vkJJPOV/DquhueGj0V/m2bOAiW4+4Dk/AZsOmrAzeh3ZV9cAv3wv0DpwNF77lwD5/PPpF/LXrgUuoBfkOaRHN+vU6c/8FjIAtIzsgduAUp1z4M0Z0h9L+zWcRbOz9NQ8texETNXAiW6/z8o5usMHK7K34WrgrWBACXCf50UC32ypbPwFhu8ndVfoHZniyuAMlL3EikAykCAjm14ndkKKPnlGc/tgQDlmc9VP5f+HSR+B1Y5lP7iwQinm9OfSNpO+hOU/HFu/NubgFID/CtpKGkSaQuUYIX3FOEZH9zx4N6tJgYOXz+ng83ldHH6OJ2cXk43p/9ZOW9cwf8DDWVvAJskpOUAAAAASUVORK5CYII=" alt="Example App Logo" width="125" height="125" />
                </div>
                <div className="flex-1 min-h-0" id="chat-ui"></div>
            </div>
    );
};
